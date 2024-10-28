package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenInfoResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.dto.oauth.request.OauthDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {
    private final KakaoOAuthAPIService kakaoOAuthAPIService;
    private final KakaoCommonAPIService kakaoCommonAPIService;

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
        KakaoTokenInfoResponse token = kakaoOAuthAPIService.getTokenInfo(
                clientId,
                clientSecret,
                dto.getCode(),
                "authorization_code",
                redirectUri
        );

        // 2. 사용자 프로필 정보 요청
        KakaoUserProfileResponse profile = kakaoCommonAPIService.getUserProfile(
                String.format("Bearer %s", token.getAccessToken())
        );

        return null;
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
