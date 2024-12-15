package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
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

import java.time.LocalDate;

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

        // 사용자 정보 저장 & 업데이트
        commonOAuthService.saveOrUpdateOAuthToken(user.getId(), dto, LocalDate.now().plusMonths(2));
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
     * @param memberId
     */
    @Override
    public void reissue(Long memberId) {

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
    public void unlink() {

    }
}
