package com.impacus.maketplace.dto.oauth.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OauthCodeDTO {
    @NotBlank
    private String code;

    @ValidEnum(enumClass = OauthProviderType.class)
    private OauthProviderType oauthProviderType;

    private String state;

    @ValidEnum(enumClass = OSType.class, nullable = true)
    private OSType os;
}
