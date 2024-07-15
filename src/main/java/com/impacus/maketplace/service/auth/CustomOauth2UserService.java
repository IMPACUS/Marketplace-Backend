package com.impacus.maketplace.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomOAuth2AuthenticationException;
import com.impacus.maketplace.common.handler.OAuth2AuthenticationFailureHandler;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.user.UserStatusInfoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import security.CustomUserDetails;
import security.SessionUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private static final String APPLE_REGISTRATION_ID = "apple";
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final UserStatusInfoService userStatusInfoService;
    private String oauthToken = "";

    public static Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);

        } catch (JsonProcessingException e) {
        }
        return jwtClaims;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        oauthToken = userRequest.getAccessToken().getTokenValue();

        OAuth2User oAuth2User;
        if (registrationId.contains(APPLE_REGISTRATION_ID)) {
            String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
            Map<String, Object> attributes = decodeJwtTokenPayload(idToken);
            attributes.put("id_token", idToken);
            Map<String, Object> userAttributes = new HashMap<>();
            userAttributes.put("resultcode", "00");
            userAttributes.put("message", "success");
            userAttributes.put("response", attributes);

            oAuth2User = new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes, "response");
        } else {
            oAuth2User = delegate.loadUser(userRequest);
        }

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
            validateOauthProvider(user, oauthProviderType);
            user = userRepository.save(user);
            userStatusInfoService.addUserStatusInfo(user.getId());
        } else {
            user = addUser(attributes);
        }

        updateRecentLoginAt(user);
        return user;
    }

    public void updateRecentLoginAt(User user) {
        user.setRecentLoginAt();
        userRepository.save(user);
    }

    private void validateOauthProvider(User user, OauthProviderType oauthProviderType) throws CustomOAuth2AuthenticationException {
        if (!user.getEmail().contains(oauthProviderType.name())) {
            throw new CustomOAuth2AuthenticationException("SERVER_ERROR", CommonErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
        }
    }

    private User addUser(OAuthAttributes attributes) {
        User newUser = attributes.toEntity();
        userRepository.save(newUser);
        return newUser;
    }
}
