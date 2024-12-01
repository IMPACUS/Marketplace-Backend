package com.impacus.maketplace.config.attribute;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            throw new CustomException(UserErrorType.NOT_ALLOW_EMAIL);
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
            throw new CustomException(UserErrorType.NOT_ALLOW_EMAIL);
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

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (Boolean.FALSE.equals((response.get("email_verified")))) {
            throw new CustomException(UserErrorType.NOT_ALLOW_EMAIL);
        }

        String email = (String) response.get("email");
        return OAuthAttributes.builder()
                .name(email)
                .email(email)
                .oAuthProvider(OauthProviderType.APPLE)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }


    public User toEntity() {
        return new User(StringUtils.createStrEmail(email, oAuthProvider), null, name);
    }
}
