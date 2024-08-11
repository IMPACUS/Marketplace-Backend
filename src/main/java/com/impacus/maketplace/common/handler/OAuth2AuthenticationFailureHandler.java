package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.exception.CustomOAuth2AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${spring.security.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String targetURL = null;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (exception.getClass() == CustomOAuth2AuthenticationException.class) {
            targetURL = determineTargetUrl(response,
                    (CustomOAuth2AuthenticationException) exception);
        } else if (exception.getClass() == OAuth2AuthenticationException.class) {
            targetURL = determineTargetUrl(response, (OAuth2AuthenticationException) exception);
        }

        getRedirectStrategy().sendRedirect(request, response, targetURL);
    }

    protected String determineTargetUrl(HttpServletResponse response,
                                        OAuth2AuthenticationException exception) {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("success", false)
                .queryParam("code", exception.getError().getErrorCode())
                .queryParam("detail", exception.getError().getDescription())
                .build().toUriString();
    }

    protected String determineTargetUrl(HttpServletResponse response,
                                        CustomOAuth2AuthenticationException exception) {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("success", false)
                .queryParam("code", exception.getErrorType().getCode())
                .queryParam("detail", exception.getError().getErrorCode())
                .build().toUriString();
    }
}
