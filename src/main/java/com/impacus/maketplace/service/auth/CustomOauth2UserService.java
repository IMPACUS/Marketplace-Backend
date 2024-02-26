package com.impacus.maketplace.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomOAuth2AuthenticationException;
import com.impacus.maketplace.common.handler.OAuth2AuthenticationFailureHandler;
import com.impacus.maketplace.config.attribute.OAuthAttributes;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import security.CustomUserDetails;
import security.SessionUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private static final String APPLE_REGISTRATION_ID = "apple";
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private String oauthToken = "";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("++++++++++++ loadUser ++++++++" + registrationId);

//        if (registrationId.contains(APPLE_REGISTRATION_ID)) {
//            Map<String, Object> attributes;
//
//            String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
//            attributes = decodeJwtTokenPayload(idToken);
//            attributes.put("id_token", idToken);
//            Map<String, Object> userAttributes = new HashMap<>();
//            userAttributes.put("resultcode", "00");
//            userAttributes.put("message", "success");
//            userAttributes.put("response", attributes);
//
//            return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes, "response");
//
//        } else {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        oauthToken = userRequest.getAccessToken().getTokenValue();

        try {
            return process(userRequest, oAuth2User);
        } catch (IOException e) {
            throw new CustomOAuth2AuthenticationException("SERVER_ERROR");
        }
//        }
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

        updateRecentLoginAt(user);
        return userRepository.save(user);
    }

    public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
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

    public void updateRecentLoginAt(User user) {
        user.setRecentLoginAt();
        userRepository.save(user);
    }
}
