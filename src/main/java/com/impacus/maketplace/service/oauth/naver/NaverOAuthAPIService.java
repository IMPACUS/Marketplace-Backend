package com.impacus.maketplace.service.oauth.naver;


import com.impacus.maketplace.common.constants.api.NaverAPIConstants;
import com.impacus.maketplace.dto.oauth.naver.NaverTokenResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverOAuthAPIClient",
        url = NaverAPIConstants.AUTH_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface NaverOAuthAPIService {

    @PostMapping(value = NaverAPIConstants.TOKEN,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    NaverTokenResponse getNaverToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );

    @PostMapping(value = NaverAPIConstants.TOKEN,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    NaverTokenResponse reissueNaverToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("grant_type") String grantType
    );
}
