package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.constants.api.KakaoAPIConstants;
import com.impacus.maketplace.dto.oauth.KakaoTokenInfoRequest;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoOAuthAPIClient",
        url = KakaoAPIConstants.OAUTH_URL,
        fallbackFactory = KakaoOAuthAPIServiceFactory.class,
        fallback = OAuthAPIServiceFallback.class
)
public interface KakaoOAuthAPIService {
    @PostMapping(KakaoAPIConstants.TOKEN)
    Object getTokenInfo(
            @RequestBody KakaoTokenInfoRequest request
    );
}
