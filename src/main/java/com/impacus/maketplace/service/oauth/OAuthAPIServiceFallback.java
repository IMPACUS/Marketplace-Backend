package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.apple.AppleTokenResponse;
import com.impacus.maketplace.dto.oauth.google.GoogleTokenResponse;
import com.impacus.maketplace.dto.oauth.google.GoogleUserInfoResponse;
import com.impacus.maketplace.dto.oauth.kakao.KakaoTokenResponse;
import com.impacus.maketplace.dto.oauth.kakao.userProfile.KakaoUserProfileResponse;
import com.impacus.maketplace.dto.oauth.naver.NaverTokenResponse;
import com.impacus.maketplace.dto.oauth.naver.userProfile.NaverUserResponse;
import com.impacus.maketplace.service.oauth.apple.AppleOAuthAPIService;
import com.impacus.maketplace.service.oauth.google.GoogleCommonAPIService;
import com.impacus.maketplace.service.oauth.google.GoogleOAuthAPIService;
import com.impacus.maketplace.service.oauth.kakao.KakaoCommonAPIService;
import com.impacus.maketplace.service.oauth.kakao.KakaoOAuthAPIService;
import com.impacus.maketplace.service.oauth.naver.NaverCommonAPIService;
import com.impacus.maketplace.service.oauth.naver.NaverOAuthAPIService;

public class OAuthAPIServiceFallback implements
        KakaoOAuthAPIService,
        KakaoCommonAPIService,
        NaverOAuthAPIService,
        NaverCommonAPIService,
        AppleOAuthAPIService,
        GoogleOAuthAPIService,
        GoogleCommonAPIService {

    @Override
    public KakaoTokenResponse getTokenInfo(
            String clientId,
            String clientSecret,
            String code,
            String grantType,
            String redirectUri
    ) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public KakaoUserProfileResponse getUserProfile(String authorization) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public NaverTokenResponse getNaverToken(String grantType, String clientId, String clientSecret, String code, String state) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public NaverUserResponse getUser(String authorization) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public AppleTokenResponse getToken(String clientId, String clientSecret, String code, String grantType) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public GoogleTokenResponse getGoogleToken(String clientId, String clientSecret, String code, String grantType, String redirectUri) {
        throw new CustomException(CommonErrorType.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(String authorization) {
        return null;
    }
}
