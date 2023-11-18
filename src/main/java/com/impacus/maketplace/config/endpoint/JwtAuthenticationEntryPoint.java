package com.impacus.maketplace.config.endpoint;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        // BadCredentialsException -> 비밀번호 틀렸을 때,
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401 -> 만료된 토큰인 경우
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //throw new CustomException(HttpStatus.NOT_FOUND ,ErrorType.EXPIRED_TOKEN);

    }

}
