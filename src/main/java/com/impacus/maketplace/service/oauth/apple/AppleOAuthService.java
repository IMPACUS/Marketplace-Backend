package com.impacus.maketplace.service.oauth.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.impacus.maketplace.common.constants.api.AppleAPIConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.apple.AppleTokenResponse;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.request.OauthTokenDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.user.User;
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

    private static final String GRANT_TYPE = "authorization_code";

    private final AppleOAuthAPIService appleOAuthAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;

    @Value("${apple.app-id}")
    private String clientId;
    @Value("${apple.teamId}")
    private String appleTeamId;
    @Value("${apple.keyId}")
    private String appleKeyId;
    @Value("${apple.keyPath}")
    private String appleKeyPath;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthCodeDTO dto) {
        // 1. 사용자 토큰 조회
        AppleTokenResponse tokenResponse = appleOAuthAPIService.getToken(
                clientId,
                createSecret(),
                dto.getCode(),
                GRANT_TYPE
        );

        // 2. 회원가입/로그인
        String email = getEmailFromIdToken(tokenResponse.getIdToken());
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name(email)
                .email(email)
                .oAuthProvider(dto.getOauthProviderType())
                .build();
        User user = customOauth2UserService.saveOrUpdate(attribute);
        Authentication auth = tokenProvider.createAuthenticationFromUser(user, UserType.ROLE_CERTIFIED_USER);
        TokenInfoVO token = tokenProvider.createToken(auth);

        return OauthLoginDTO.of(
                user,
                false,
                token
        );
    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    public OauthLoginDTO login(OauthTokenDTO dto) {
        throw new CustomException(CommonErrorType.UNKNOWN, "아직 제공하지 않는 기능입니다.");
    }

    private String getEmailFromIdToken(String idToken) {
        DecodedJWT jwt = JWT.decode(idToken);

        // 사용자 정보 추출
        String userId = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        boolean emailVerified = jwt.getClaim("email_verified").asBoolean();

        return email;
    }

    private String createSecret() {
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
     * @param memberId
     */
    @Override
    public void reissue(Long memberId) {

    }

    /**
     * 소셜 로그인 연동해제
     */
    @Override
    public void unlink() {

    }
}
