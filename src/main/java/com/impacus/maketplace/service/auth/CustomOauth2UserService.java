package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomOAuth2AuthenticationException;
import com.impacus.maketplace.common.handler.OAuth2AuthenticationFailureHandler;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import security.CustomUserDetails;
import security.SessionUser;
import com.impacus.maketplace.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;

    private String oauthToken = "";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        oauthToken = userRequest.getAccessToken().getTokenValue();

        try {
            return process(userRequest, oAuth2User);
        } catch (IOException e) {
            throw new CustomOAuth2AuthenticationException("SERVER_ERROR");
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User)
        throws IOException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        return CustomUserDetails.create(user, oAuth2User.getAttributes());
    }

    public User saveOrUpdate(OAuthAttributes attributes)
        throws CustomOAuth2AuthenticationException {
        OauthProviderType oauthProviderType = attributes.getOAuthProvider();
        String email = attributes.getEmail();

        // 1. 이메일이 등록되어 있는지 확인
        List<User> userList = userRepository.findByEmailLike("%_" + email);

        // 2. 등록되지 않은 경우: 저장 / 다른 제공사로 등록되어 있는 경우 예외 발생
        User user = null;
        if (!userList.isEmpty()) {
            user = userList.get(0);
            if (!user.getEmail().contains(oauthProviderType.name())) {
                throw new CustomOAuth2AuthenticationException("SERVER_ERROR",
                    ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        } else {
            user = attributes.toEntity();
        }

        return userRepository.save(user);
    }
}
