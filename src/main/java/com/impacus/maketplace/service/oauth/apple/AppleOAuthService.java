package com.impacus.maketplace.service.oauth.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.impacus.maketplace.common.constants.api.AppleAPIConstants;
import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.apple.AppleTokenResponse;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.consumer.oAuthToken.AppleOAuthToken;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.service.oauth.CommonOAuthService;
import com.impacus.maketplace.service.oauth.CustomOauth2UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppleOAuthService implements OAuthService {

    private static final String GENERATE_TOKEN_GRANT_TYPE = "authorization_code";
    private static final String REISSUE_GRANT_TYPE = "refresh_token";

    private final AppleOAuthAPIService appleOAuthAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;
    private final CommonOAuthService commonOAuthService;

    @Value("${apple.aos-app-id}")
    private String aosClientId;
    @Value("${apple.ios-app-id}")
    private String iosClientId;
    @Value("${apple.teamId}")
    private String appleTeamId;
    @Value("${apple.keyId}")
    private String appleKeyId;
    @Value("${apple.keyPath}")
    private String appleKeyPath;

    private String getClientId(OSType osType) {
        if (osType == OSType.AOS) {
            return aosClientId;
        } else {
            return iosClientId;
        }
    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthCodeDTO dto) {
        if (dto.getOs() == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "os 데이터가 null일 수 없습니다.");
        }

        // 1. 사용자 토큰 조회
        String clientId = getClientId(dto.getOs());
        AppleTokenResponse tokenResponse = appleOAuthAPIService.getToken(
                clientId,
                createSecret(clientId),
                dto.getCode(),
                GENERATE_TOKEN_GRANT_TYPE
        );

        // 2. 회원가입/로그인
        OAuthTokenDTO oauthTokenDTO = OAuthTokenDTO.toDTO(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        return saveOrUpdateUser(tokenResponse.getIdToken(), oauthTokenDTO);
    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OAuthTokenDTO dto) {
        if (dto.getOs() == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "os 데이터가 null일 수 없습니다.");
        }

        // 1. 사용자 정보 조회
        String clientId = getClientId(dto.getOs());
        AppleTokenResponse tokenResponse = appleOAuthAPIService.reissueAppleToken(
                clientId,
                createSecret(clientId),
                dto.getRefreshToken(),
                REISSUE_GRANT_TYPE
        );

        return saveOrUpdateUser(tokenResponse.getIdToken(), dto);
    }

    /**
     * 애플 사용자를 저장 혹은 업데이트하고 토큰 발급하는 함수
     *
     * @param idToken
     * @return
     */
    @Transactional
    public OauthLoginDTO saveOrUpdateUser(String idToken, OAuthTokenDTO dto) {
        String email = getEmailFromIdToken(idToken);
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name(email)
                .email(email)
                .oAuthProvider(OauthProviderType.APPLE)
                .build();
        User user = customOauth2UserService.saveOrUpdate(attribute);

        // 사용자 정보 저장 & 업데이트
        commonOAuthService.saveOrUpdateOAuthToken(user.getId(), dto, dto.getOs());
        Authentication auth = tokenProvider.createAuthenticationFromUser(user, UserType.ROLE_CERTIFIED_USER);
        TokenInfoVO token = tokenProvider.createToken(auth);

        return OauthLoginDTO.of(
                user,
                false,
                token
        );
    }

    private String getEmailFromIdToken(String idToken) {
        DecodedJWT jwt = JWT.decode(idToken);

        // 사용자 정보 추출
        String userId = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        boolean emailVerified = jwt.getClaim("email_verified").asBoolean();

        return email;
    }

    private String createSecret(String clientId) {
        Date expirationDate = Date.from(
                LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        try {
            return Jwts.builder()
                    .setHeaderParam("alg", "ES256")
                    .setHeaderParam("kid", appleKeyId)
                    .setIssuer(appleTeamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience(AppleAPIConstants.COMMON_URL)
                    .setSubject(clientId)
                    .signWith(this.getPrivateKey(), SignatureAlgorithm.ES256)
                    .compact();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/key/" + appleKeyPath);

        try (InputStream in = resource.getInputStream();
             PEMParser pemParser = new PEMParser(
                     new InputStreamReader(in, StandardCharsets.UTF_8))) {
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPrivateKey(object);
        }
    }

    /**
     * 소셜 로그인 토큰 재발급
     *
     * @param userId
     */
    @Override
    public OAuthTokenDTO reissue(Long userId) {
        // OAuth 토큰 조회
        AppleOAuthToken oAuthToken = (AppleOAuthToken) commonOAuthService.findOAuthTokenByUserId(userId);

        // 토큰 갱신 요청
        String clientId = getClientId(oAuthToken.getOsType());
        AppleTokenResponse tokenResponse = appleOAuthAPIService.reissueAppleToken(
                clientId,
                createSecret(clientId),
                oAuthToken.getRefreshToken(),
                REISSUE_GRANT_TYPE
        );

        return tokenResponse.toOAuthTokenDTO(oAuthToken.getOsType());
    }

    /**
     * 소셜 로그인 연동해제
     */
    @Override
    public void unlink(Long userId) {
        // 토큰 갱신
        OAuthTokenDTO oAuthToken = this.reissue(userId);

        // 연동 해제
        String clientId = getClientId(oAuthToken.getOs());
        appleOAuthAPIService.unlinkApple(
                clientId,
                createSecret(clientId),
                oAuthToken.getAccessToken()
        );
    }
}
