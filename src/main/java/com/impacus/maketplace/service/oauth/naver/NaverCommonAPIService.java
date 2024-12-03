package com.impacus.maketplace.service.oauth.naver;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.constants.api.NaverAPIConstants;
import com.impacus.maketplace.dto.oauth.naver.userProfile.NaverUserResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverCommonAPIClient",
        url = NaverAPIConstants.COMMON_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface NaverCommonAPIService {

    @GetMapping(value = NaverAPIConstants.USER_INFO,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    NaverUserResponse getUser(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization
    );
}
