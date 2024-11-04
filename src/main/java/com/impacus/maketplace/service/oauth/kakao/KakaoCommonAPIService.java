package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.constants.api.KakaoAPIConstants;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoCommonAPIClient",
        url = KakaoAPIConstants.COMMON_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface KakaoCommonAPIService {

    @GetMapping(KakaoAPIConstants.USER)
    KakaoUserProfileResponse getUserProfile(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization
    );
}
