package com.impacus.maketplace.config.attribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@NoArgsConstructor
public class OAuthAttributes {

    private static final String NAVER_STRING_KEY = "naver";
    private static final String KAKAO_STRING_KEY = "kakao";
    private static final String GOOGLE_STRING_KEY = "google";
    private static final String APPLE_STRING_KEY = "apple";

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private OauthProviderType oAuthProvider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name,
                           String email, OauthProviderType oAuthProvider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.oAuthProvider = oAuthProvider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        log.info("IN ------ OAuthAttributes of ------  " + registrationId);
        if (NAVER_STRING_KEY.equals(registrationId)) {
            return ofNaver("id", attributes);
        } else if (KAKAO_STRING_KEY.equals(registrationId)) {
            return ofKakao("id", attributes);
        } else if (GOOGLE_STRING_KEY.equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        } else {
            return ofApple(userNameAttributeName, attributes);
        }
    }

    private static OAuthAttributes ofNaver(String nameAttributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .oAuthProvider(OauthProviderType.NAVER)
                .attributes(response)
                .nameAttributeKey(nameAttributeKey)
                .build();

    }

    private static OAuthAttributes ofGoogle(String nameAttributeKey,
                                            Map<String, Object> attributes) {

        if (Boolean.FALSE.equals((attributes.get("email_verified")))) {
            throw new CustomException(ErrorType.NOT_ALLOW_EMAIL);
        }

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .oAuthProvider(OauthProviderType.GOOGLE)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    private static OAuthAttributes ofKakao(String nameAttributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProperty = (Map<String, Object>) attributes.get("properties");

        if (Boolean.FALSE.equals((kakaoAccount.get("has_email")))) {
            throw new CustomException(ErrorType.NOT_ALLOW_EMAIL);
        }

        return OAuthAttributes.builder()
                .name((String) kakaoProperty.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .oAuthProvider(OauthProviderType.KAKAO)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    private static OAuthAttributes ofApple(String nameAttributeKey,
                                           Map<String, Object> attributes) {
        log.info("IN ========= ofApple");
        log.info(attributes.toString());

        String idToken = attributes.get("id_token").toString();
        Map<String, Object> payload = decodeJwtTokenPayload(idToken);
        payload.put("id_token", idToken);
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("resultcode", "00");
        userAttributes.put("message", "success");
        userAttributes.put("response", attributes);

//        return OAuthAttributes.builder()
//                .name((String) kakaoProperty.get("nickname"))
//                .email((String) kakaoAccount.get("email"))
//                .oAuthProvider(OauthProviderType.KAKAO)
//                .attributes(attributes)
//                .nameAttributeKey(nameAttributeKey)
//                .build();
        return null;

        //return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes, "response");
    }

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


    public User toEntity() {
        return new User(StringUtils.createStrEmail(email, oAuthProvider), null, name);
    }
}
