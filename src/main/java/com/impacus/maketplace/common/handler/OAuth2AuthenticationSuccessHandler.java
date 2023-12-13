package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        String targetURL = determineTargetUrl(response, authentication);

        getRedirectStrategy().sendRedirect(request, response, targetURL);
    }

    protected String determineTargetUrl(HttpServletResponse response,
        Authentication authentication) {
        TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);
        String jsonResponse = "{\"accessToken\": \"" + tokenInfoVO.accessToken() + "\"}";

        try {
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("success", true)
            .queryParam("accessToken", tokenInfoVO.accessToken())
            .queryParam("refreshToken", tokenInfoVO.refreshToken())
            .build().toUriString();
    }
}
