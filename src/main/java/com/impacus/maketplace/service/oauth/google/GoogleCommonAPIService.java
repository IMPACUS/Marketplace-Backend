package com.impacus.maketplace.service.oauth.google;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.constants.api.GoogleAPIConstants;
import com.impacus.maketplace.dto.oauth.google.GoogleUserInfoResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleCommonApiClient",
        url = GoogleAPIConstants.COMMON_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface GoogleCommonAPIService {
    @GetMapping(value = GoogleAPIConstants.USER_INFO,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    GoogleUserInfoResponse getUserInfo(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization);
}
