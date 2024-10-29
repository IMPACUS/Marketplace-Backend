package com.impacus.maketplace.service.oauth.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenInfoResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.dto.oauth.request.OauthDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.service.oauth.CustomOauth2UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {
    private final KakaoOAuthAPIService kakaoOAuthAPIService;
    private final KakaoCommonAPIService kakaoCommonAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    public OauthLoginDTO login(OauthDTO dto) {
        // 1. Kakao 토큰 요청
        KakaoTokenInfoResponse tokenResponse = kakaoOAuthAPIService.getTokenInfo(
                clientId,
                clientSecret,
                dto.getCode(),
                "authorization_code",
                redirectUri
        );

        // 2. 사용자 프로필 정보 요청
        KakaoUserProfileResponse profileResponse = kakaoCommonAPIService.getUserProfile(
                String.format("Bearer %s", tokenResponse.getAccessToken())
        );

        // 3. 회원가입/로그인
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name(profileResponse.getKakaoAccount().getName())
                .email(profileResponse.getKakaoAccount().getEmail())
                .build();
        User user = customOauth2UserService.saveOrUpdate(attribute);
        Authentication auth = tokenProvider.createAuthenticationFromUser(user, UserType.ROLE_CERTIFIED_USER);
        TokenInfoVO token = tokenProvider.createToken(auth);

        return OauthLoginDTO.builder()
                .user(user)
                .hasSignedUp(false)
                .token(token)
                .build();
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
