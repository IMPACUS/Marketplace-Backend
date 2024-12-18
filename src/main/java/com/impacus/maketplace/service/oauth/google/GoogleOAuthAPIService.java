package com.impacus.maketplace.service.oauth.google;

import com.impacus.maketplace.common.constants.api.AppleAPIConstants;
import com.impacus.maketplace.common.constants.api.GoogleAPIConstants;
import com.impacus.maketplace.dto.oauth.google.GoogleTokenResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleOAuthApiClient",
        url = GoogleAPIConstants.AUTH_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface GoogleOAuthAPIService {

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    GoogleTokenResponse getGoogleToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri
    );

    @PostMapping(value = GoogleAPIConstants.TOKEN,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    GoogleTokenResponse reissueGoogleToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken
    );

    @PostMapping(value = GoogleAPIConstants.REVOKE,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    void unlinkGoogle(
            @RequestParam("token") String token
    );
}
