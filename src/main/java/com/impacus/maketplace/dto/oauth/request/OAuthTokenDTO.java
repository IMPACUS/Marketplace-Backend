package com.impacus.maketplace.dto.oauth.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.entity.consumer.oAuthToken.AppleOAuthToken;
import com.impacus.maketplace.entity.consumer.oAuthToken.CommonOAuthToken;
import com.impacus.maketplace.entity.consumer.oAuthToken.KakaoOAuthToken;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthTokenDTO {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @ValidEnum(enumClass = OauthProviderType.class)
    private OauthProviderType oauthProviderType;

    @ValidEnum(enumClass = OSType.class, nullable = true)
    private OSType os;

    public OAuthTokenDTO(String accessToken,
                         String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public OAuthTokenDTO(String accessToken,
                         String refreshToken,
                         OSType os
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.os = os;
    }

    public static OAuthTokenDTO toDTO(String accessToken, String refreshToken) {
        return new OAuthTokenDTO(accessToken, refreshToken);
    }

    public static OAuthTokenDTO toDTO(
            String accessToken, String refreshToken, OauthProviderType oauthProviderType
    ) {
        return new OAuthTokenDTO(accessToken, refreshToken, oauthProviderType, null);
    }

    public CommonOAuthToken toEntity(Long consumerId) {
        return new CommonOAuthToken(
                consumerId,
                this.accessToken,
                this.refreshToken
        );
    }

    public KakaoOAuthToken toEntity(Long consumerId, Long oAuthUserId) {
        return new KakaoOAuthToken(
                consumerId,
                this.accessToken,
                this.refreshToken,
                oAuthUserId
        );
    }

    public AppleOAuthToken toEntity(Long consumerId, OSType osType) {
        return new AppleOAuthToken(
                consumerId,
                this.accessToken,
                this.refreshToken,
                osType
        );
    }
}
