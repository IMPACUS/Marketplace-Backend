package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.redis.service.BlacklistService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final BlacklistService blacklistService;

    private final static String AUTHENTICATION_HEADER_TYPE = "Bearer";

    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserDTO reissueToken(String accessToken, String refreshToken) {
        accessToken = StringUtils.parseGrantTypeInToken(AUTHENTICATION_HEADER_TYPE, accessToken);

        try {
            // 1. refresh token 유효성 확인
            TokenErrorType validateResult = tokenProvider.validateToken(refreshToken);
            if (validateResult != TokenErrorType.NONE) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, validateResult.getErrorType());
            }

            // 2. Access token 사용자 확인
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findUserByEmail(customUserDetail.getEmail());

            // 3. JWT 토큰 재발급
            TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);

            return new UserDTO(user, tokenInfoVO);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 로그아웃 함수
     *
     * @param accessToken
     */
    @Transactional
    public void logout(String accessToken) {
        accessToken = StringUtils.parseGrantTypeInToken(AUTHENTICATION_HEADER_TYPE, accessToken);
        Long expiration = tokenProvider.getExpiration(accessToken);
        if (expiration == null || expiration == 0L) {
            return;
        }

        blacklistService.saveBlacklist(accessToken, expiration);
    }
}
