package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.service.UserService;
import javax.swing.text.html.Option;
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
@Transactional(readOnly = true)
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    private String oauthToken = "";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        oauthToken = userRequest.getAccessToken().getTokenValue();

        return process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        return CustomUserDetails.create(user, oAuth2User.getAttributes());
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        OauthProviderType oauthProviderType = attributes.getOAuthProvider();
        String emailWithPrefix = StringUtils.createStrEmail(attributes.getEmail(),
            oauthProviderType);

        User user = userRepository.findByEmailLike(emailWithPrefix)
            .orElse(attributes.toEntity());

        if (!user.getEmail().contains(oauthProviderType.name())) {
            throw new CustomException(ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
        }

        return userRepository.save(user);
    }
}
