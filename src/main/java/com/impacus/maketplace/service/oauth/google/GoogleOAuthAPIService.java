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

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE)
    GoogleTokenResponse getGoogleToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri
    );
}
