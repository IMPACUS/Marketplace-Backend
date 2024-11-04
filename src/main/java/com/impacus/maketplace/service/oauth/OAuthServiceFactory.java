package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.service.oauth.apple.AppleOAuthService;
import com.impacus.maketplace.service.oauth.google.GoogleOAuthService;
import com.impacus.maketplace.service.oauth.kakao.KakaoOAuthService;
import com.impacus.maketplace.service.oauth.naver.NaverOAuthService;
import org.springframework.stereotype.Component;

@Component
public class OAuthServiceFactory {

    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;
    private final NaverOAuthService naverOAuthService;
    private final KakaoOAuthService kakaoOAuthService;

    public OAuthServiceFactory(
            GoogleOAuthService googleOAuthService,
            AppleOAuthService appleOAuthService,
            NaverOAuthService naverOAuthService,
            KakaoOAuthService kakaoOAuthService
    ) {
        this.googleOAuthService = googleOAuthService;
        this.appleOAuthService = appleOAuthService;
        this.naverOAuthService = naverOAuthService;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    public OAuthService getService(OauthProviderType providerType) {
        switch (providerType) {
            case GOOGLE:
                return googleOAuthService;
            case APPLE:
                return appleOAuthService;
            case KAKAO:
                return kakaoOAuthService;
            case NAVER:
                return naverOAuthService;
            default:
                throw new IllegalArgumentException("Unknown provider type: " + providerType);
        }
    }
}
