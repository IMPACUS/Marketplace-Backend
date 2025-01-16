package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.constants.api.KakaoAPIConstants;
import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoOAuthAPIClient",
        url = KakaoAPIConstants.OAUTH_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface KakaoOAuthAPIService {
    @PostMapping(value = KakaoAPIConstants.TOKEN,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoTokenResponse getTokenInfo(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri
    );

    @PostMapping(value = KakaoAPIConstants.TOKEN,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoTokenResponse reissueKakaoToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_secret") String clientSecret
    );
}
