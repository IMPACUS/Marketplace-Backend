package com.impacus.maketplace.dto.oauth.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OauthTokenDTO {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @ValidEnum(enumClass = OauthProviderType.class)
    private OauthProviderType oauthProviderType;
}
