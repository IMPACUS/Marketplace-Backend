package com.impacus.maketplace.dto.oauth.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.entity.consumer.OAuthToken;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

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

    public static OAuthTokenDTO toDTO(String accessToken, String refreshToken) {
        return new OAuthTokenDTO(accessToken, refreshToken);
    }

    public static OAuthTokenDTO toDTO(
            String accessToken, String refreshToken, OauthProviderType oauthProviderType
    ) {
        return new OAuthTokenDTO(accessToken, refreshToken, oauthProviderType, null);
    }

    public OAuthToken toEntity(Long consumerId, LocalDate refreshTokenExpiresIn) {
        return new OAuthToken(
                consumerId,
                this.accessToken,
                this.refreshToken,
                refreshTokenExpiresIn
        );
    }
}
