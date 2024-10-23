package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.KakaoTokenInfoRequest;
import com.impacus.maketplace.service.oauth.kakao.KakaoOAuthAPIService;

public class OAuthAPIServiceFallback implements KakaoOAuthAPIService {
    @Override
    public Object getTokenInfo(KakaoTokenInfoRequest request) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }
}
