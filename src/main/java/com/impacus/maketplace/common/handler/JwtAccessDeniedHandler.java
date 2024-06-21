package com.impacus.maketplace.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.dto.error.response.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        CommonErrorType errorType = CommonErrorType.ACCESS_DENIED_ACCOUNT;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String errorDetail = getErrorDetail(authentication);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(errorType.getCode())
                .msg(errorType.getMsg())
                .detail(errorDetail)
                .build();

        String errorJson = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(errorJson);
    }

    private String getErrorDetail(Authentication authentication) {
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            return String.format("%s 권한을 가진 사용자는 해당 API에 접근할 수 없습니다.", roles);
        } else {
            return "Access token 이 필요합니다.";
        }
    }

}
