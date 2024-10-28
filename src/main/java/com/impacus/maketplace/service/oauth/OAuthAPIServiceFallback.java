package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.KakaoTokenInfoResponse;
import com.impacus.maketplace.service.oauth.kakao.KakaoOAuthAPIService;

public class OAuthAPIServiceFallback implements KakaoOAuthAPIService {

    @Override
    public KakaoTokenInfoResponse getTokenInfo(
            String clientId,
            String clientSecret,
            String code,
            String grantType,
            String redirectUri
    ) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }
}
