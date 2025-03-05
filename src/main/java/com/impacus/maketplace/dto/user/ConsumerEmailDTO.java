package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConsumerEmailDTO {
    private Long userId;
    private String email;
    private OauthProviderType oauthProviderType;
    private String password;

    public ConsumerEmailDTO(Long userId, String email, String password, OauthProviderType oauthProviderType) {
        this.userId = userId;
        this.email = email;
        this.oauthProviderType = oauthProviderType;
        this.password = password;
    }
}
