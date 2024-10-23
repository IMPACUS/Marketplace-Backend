package com.impacus.maketplace.service.oauth.kakao;

import com.impacus.maketplace.dto.oauth.request.OauthDTO;
import com.impacus.maketplace.dto.oauth.response.OauthLoginDTO;
import com.impacus.maketplace.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    /**
     * 소셜 로그인/소셜 로그인 회원가입
     *
     * @param dto
     */
    @Override
    public OauthLoginDTO login(OauthDTO dto) throws IOException {
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
