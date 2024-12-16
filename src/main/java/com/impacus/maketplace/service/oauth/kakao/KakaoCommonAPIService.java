package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.constants.api.KakaoAPIConstants;
import com.impacus.maketplace.dto.oauth.kakao.KakaoUnlinkResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.service.oauth.OAuthAPIServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoCommonAPIClient",
        url = KakaoAPIConstants.COMMON_URL,
        fallback = OAuthAPIServiceFallback.class
)
public interface KakaoCommonAPIService {

    @GetMapping(value = KakaoAPIConstants.USER,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserProfileResponse getUserProfile(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization
    );

    @PostMapping(value = KakaoAPIConstants.UNLINK,
            headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoUnlinkResponse unlinkKakao(
            @RequestHeader(HeaderConstants.AUTHORIZATION_HEADER) String authorization,
            @RequestParam("target_id_type") String targetIdType,
            @RequestParam("target_id") Long targetId
    );
}
