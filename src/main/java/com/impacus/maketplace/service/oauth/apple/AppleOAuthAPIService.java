package com.impacus.maketplace.service.oauth.apple;

import com.impacus.maketplace.common.constants.api.AppleAPIConstants;
import com.impacus.maketplace.dto.oauth.apple.AppleTokenResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appleApiClient",
        url = AppleAPIConstants.AUTH_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface AppleOAuthAPIService {

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE)
    AppleTokenResponse getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType
    );

    @PostMapping(value = AppleAPIConstants.VALIDATE_CODE)
    AppleTokenResponse reissueToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("grant_type") String grantType
    );
}
