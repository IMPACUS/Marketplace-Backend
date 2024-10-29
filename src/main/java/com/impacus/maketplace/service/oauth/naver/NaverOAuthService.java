package com.impacus.maketplace.service.oauth.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.naver.NaverTokenResponse;
import com.impacus.maketplace.dto.oauth.request.OauthDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.service.oauth.OAuthService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {
    private final NaverOAuthAPIService naverOAuthAPIService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    public OauthLoginDTO login(OauthDTO dto) {
        if (dto.getState() == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "Naver 로그인인 경우, state가 null일 수 없습니다.");
        }

        // 1. Naver 토큰 발급
        NaverTokenResponse tokenResponse = naverOAuthAPIService.getToken(
            "authorization_code", 
                        clientId, 
                        clientSecret,
                        dto.getCode(),
                        dto.getState()
        );

        return null;
    }

    /**
     * 소셜 로그인 토큰 재발급
     *
     * @param memberId
     */
    @Override
    public void reissue(Long memberId) {

    }

    /**
     * 소셜 로그인 연동해제
     */
    @Override
    public void unlink() {

    }
}
