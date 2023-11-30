package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.entity.vo.auth.TokenInfoVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        TokenInfoVO tokenInfoVO = tokenProvider.createToken(authentication);
        String jsonResponse = "{\"token\": \"" + tokenInfoVO.getAccessToken() + "\"}";

        response.getWriter().write(jsonResponse);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
