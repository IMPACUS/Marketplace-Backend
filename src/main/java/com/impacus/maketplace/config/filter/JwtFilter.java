package com.impacus.maketplace.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.error.response.ErrorDTO;
import com.impacus.maketplace.redis.service.BlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;
    private final BlacklistService blacklistService;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, CustomException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String jwtAccessToken = parseBearerToken(httpServletRequest);

        try {
            if (StringUtils.hasText(jwtAccessToken) && tokenProvider.validateToken(jwtAccessToken) == TokenErrorType.NONE) {
                if (!checkIsLogout(jwtAccessToken)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwtAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new CustomException(CommonErrorType.LOGGED_OUT_TOKEN);
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (CustomException e) {
            setErrorResponse(httpServletResponse, e.getErrorType());
        } catch (Exception e) {
            TokenErrorType tokenErrorType = tokenProvider.validateToken(jwtAccessToken);
            CommonErrorType errorType = tokenErrorType == TokenErrorType.EXPIRED_TOKEN
                    ? CommonErrorType.EXPIRED_TOKEN
                    : CommonErrorType.INVALID_TOKEN;
            setErrorResponse(httpServletResponse, errorType);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorType errorType) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorDTO errorDTO = ErrorDTO.toDTO(errorType);

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(errorJson);
    }

    /**
     * 로그아웃된 토큰인지 확인하는 함수
     *
     * @param accessToken
     * @return
     */
    private boolean checkIsLogout(String accessToken) {
        return blacklistService.existsBlacklistByAccessToken(accessToken);
    }

    /**
     * Request Header 에서 토큰 정보를 꺼내오기 위한 함수
     *
     * @param request
     * @return
     */
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HeaderConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
