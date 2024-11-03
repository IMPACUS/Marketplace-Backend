package com.impacus.maketplace.service.oauth.google;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.oauth.google.GoogleTokenResponse;
import com.impacus.maketplace.dto.oauth.google.GoogleUserInfoResponse;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.request.OauthTokenDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.service.oauth.CustomOauth2UserService;
import com.impacus.maketplace.service.oauth.OAuthService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {
    private final GoogleOAuthAPIService googleOAuthAPIService;
    private final GoogleCommonAPIService googleCommonAPIService;
    private final CustomOauth2UserService customOauth2UserService;
    private final JwtTokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthCodeDTO dto) {
        // 1. 토큰 정보 요청
        GoogleTokenResponse tokenResponse = googleOAuthAPIService.getGoogleToken(
                clientId,
                clientSecret,
                dto.getCode(),
                "authorization_code",
                redirectUri
        );

        OauthTokenDTO tokenRequestDTO = OauthTokenDTO.toDTO(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                dto.getOauthProviderType()
        );

        return login(tokenRequestDTO);
    }

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    @Transactional
    public OauthLoginDTO login(OauthTokenDTO dto) {
        // 1. 사용자 정보 요청
        GoogleUserInfoResponse userInfoResponse = googleCommonAPIService.getUserInfo(
                String.format("Bearer %s", dto.getAccessToken())
        );

        // 2. 회원 가입 및 로그인
        OAuthAttributes attribute = OAuthAttributes.builder()
                .name(userInfoResponse.getName())
                .email(userInfoResponse.getEmail())
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
