package com.impacus.maketplace.config.filter;

import com.impacus.maketplace.common.constants.HeaderConstants;
import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.redis.service.BlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

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
    ) throws IOException, CustomException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwtAccessToken = parseBearerToken(httpServletRequest);

        if (StringUtils.hasText(jwtAccessToken)) {
                TokenErrorType tokenError = tokenProvider.validateToken(jwtAccessToken);

                switch (tokenError) {
                    case NONE:
                        if (!checkIsLogout(jwtAccessToken)) {
                            Authentication authentication = tokenProvider.getAuthentication(jwtAccessToken);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            SecurityContextHolder.clearContext();
                        }
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);

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
