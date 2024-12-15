package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.dto.oauth.request.OauthCodeDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;

public interface OAuthService {

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     */
    OauthLoginDTO login(OauthCodeDTO dto);

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     */
    OauthLoginDTO login(OAuthTokenDTO dto);

    /**
     * 소셜 로그인 토큰 재발급
     */
    OAuthTokenDTO reissue(Long memberId);


    /**
     * 소셜 로그인 연동해제
     */
    void unlink(Long memberId);

}