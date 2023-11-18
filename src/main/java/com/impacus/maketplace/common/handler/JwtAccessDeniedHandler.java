package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) {
        throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.ACESS_DENIED_EMAIL);
    }

}
