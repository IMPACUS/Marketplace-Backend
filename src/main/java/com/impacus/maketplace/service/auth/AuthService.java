package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.vo.auth.TokenInfoVO;
import com.impacus.maketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public UserDTO reissueToken(String accessToken, String refreshToken) {
        accessToken = StringUtils.parseGrantTypeInToken("Bearer", accessToken);

        // 1. refresh token 유효성 확인
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.EXPIRED_REFRESH_TOKEN);
        }

        // 2. Access token 사용자 확인
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findUserByEmail(customUserDetail.getEmail());

        // 3. JWT 토큰 재발급
        TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);

        return new UserDTO(user, tokenInfoVO);
    }
}
