package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenResponse;
import com.impacus.maketplace.dto.oauth.kakao.KakaoUnlinkResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.consumer.oAuthToken.KakaoOAuthToken;
import com.impacus.maketplace.entity.consumer.oAuthToken.OAuthToken;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.service.oauth.CommonOAuthService;
import com.impacus.maketplace.service.oauth.CustomOauth2UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {
    private final KakaoOAuthAPIService kakaoOAuthAPIService;
    private final KakaoCommonAPIService kakaoCommonAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;
    private final CommonOAuthService commonOAuthService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${key.kakao.admin-key}")
    private String adminKey;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthCodeDTO dto) {
        // 1. Kakao 토큰 요청
        KakaoTokenResponse tokenResponse = kakaoOAuthAPIService.getTokenInfo(
                clientId,
                clientSecret,
                dto.getCode(),
                "authorization_code",
                redirectUri
        );

        OAuthTokenDTO tokenRequestDTO = OAuthTokenDTO.toDTO(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                dto.getOauthProviderType()
        );

        return this.login(tokenRequestDTO);
    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OAuthTokenDTO dto) {
        // 1. 사용자 프로필 정보 요청
        KakaoUserProfileResponse profileResponse = kakaoCommonAPIService.getUserProfile(
                String.format("Bearer %s", dto.getAccessToken())
        );

        // 2. 회원가입/로그인
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name((String) profileResponse.getKakaoAccount().getProfile().get("nickname"))
                .email(profileResponse.getKakaoAccount().getEmail())
                .oAuthProvider(dto.getOauthProviderType())
                .build();
        User user = customOauth2UserService.saveOrUpdate(attribute);

        // OAuth token 저장
        commonOAuthService.saveOrUpdateOAuthToken(user.getId(), dto, profileResponse.getId());

        // 사용자 정보 저장 & 업데이트
        Authentication auth = tokenProvider.createAuthenticationFromUser(user, UserType.ROLE_CERTIFIED_USER);
        TokenInfoVO token = tokenProvider.createToken(auth);

        return OauthLoginDTO.of(
                user,
                false,
                token
        );
    }

    /**
     * 소셜 로그인 토큰 재발급
     *
     * @param userId
     */
    @Override
    public OAuthTokenDTO reissue(Long userId) {
        // OAuth 토큰 조회
        OAuthToken oAuthToken = commonOAuthService.findOAuthTokenByUserId(userId);

        // 토큰 갱신 요청
        KakaoTokenResponse tokenResponse = requestReissue(oAuthToken.getRefreshToken());

        // 토큰 업데이트
        return tokenResponse.toOAuthTokenDTO();
    }

    private KakaoTokenResponse requestReissue(String refreshToken) {
        return kakaoOAuthAPIService.reissueKakaoToken(
                "refresh_token",
                clientId,
                refreshToken,
                clientSecret
        );
    }

    /**
     * 소셜 로그인 연동해제
     */
    @Override
    public void unlink(Long userId) {
        // 토큰 조회
        KakaoOAuthToken oAuthToken = (KakaoOAuthToken) commonOAuthService.findOAuthTokenByUserId(userId);

        // 연동 해제
        KakaoUnlinkResponse response = kakaoCommonAPIService.unlinkKakao(
                String.format("KakaoAK %s", adminKey),
                "user_id",
                oAuthToken.getOAuthUserId()
        );
    }
}
