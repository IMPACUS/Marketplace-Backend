package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailInfoDTO {
    private String email;
    private OauthProviderType oauthProviderType;

    public static EmailInfoDTO of(
            String email,
            OauthProviderType oauthProviderType
    ) {
        return new EmailInfoDTO(email, oauthProviderType);
    }
}
