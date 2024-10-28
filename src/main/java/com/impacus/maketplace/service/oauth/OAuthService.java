package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.dto.oauth.request.OauthDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;

public interface OAuthService {

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     */
    OauthLoginDTO login(OauthDTO dto);


    /**
     * 소셜 로그인 토큰 재발급
     */
    void reissue(Long memberId);


    /**
     * 소셜 로그인 연동해제
     */
    void unlink();

}