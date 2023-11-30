package com.impacus.maketplace.config.attribute;

import com.impacus.maketplace.common.enumType.OauthProviderType;
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
        if (KAKAO_STRING_KEY.equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(String nameAttributeKey,
        Map<String, Object> attributes) {
        return null;
    }

    private static OAuthAttributes ofGoogle(String nameAttributeKey,
        Map<String, Object> attributes) {
        return null;
    }

    private static OAuthAttributes ofKakao(String nameAttributeKey,
        Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProperty = (Map<String, Object>) attributes.get("properties");

        return OAuthAttributes.builder()
            .name((String) kakaoAccount.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .oAuthProvider(OauthProviderType.KAKAO)
            .attributes(attributes)
            .nameAttributeKey(nameAttributeKey)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .name(name)
            .email(StringUtils.createStrEmail(email, oAuthProvider))
            .build();
    }
}
