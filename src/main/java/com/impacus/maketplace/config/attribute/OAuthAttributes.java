package com.impacus.maketplace.config.attribute;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthAttributes {

    private final static String NAVER_STRING_KEY = "naver";
    private final static String KAKAO_STRING_KEY = "kakao";
    private final static String GOOGLE_STRING_KEY = "google";

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
        }
        else if (KAKAO_STRING_KEY.equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        else if (GOOGLE_STRING_KEY.equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        else {
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
        return null;
    }

    public User toEntity() {
        return new User(StringUtils.createStrEmail(email, oAuthProvider), null, name);
    }
}
