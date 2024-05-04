package com.impacus.maketplace.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.dto.error.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        CommonErrorType errorType = CommonErrorType.ACCESS_DENIED_EMAIL;

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(errorType.getCode())
                .msg(errorType.getMsg())
                .detail(accessDeniedException.getMessage())
                .build();

        String errorJson = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(errorJson);
    }

}
