package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConsumerEmailDTO {
    private Long userId;
    private String email;
    private OauthProviderType oauthProviderType;
    private String password;

    public ConsumerEmailDTO(Long userId, String email, String password) {
        EmailInfoDTO emailInfoDTO = StringUtils.getEmailInfo(email);

        this.userId = userId;
        this.email = emailInfoDTO.getEmail();
        this.oauthProviderType = emailInfoDTO.getOauthProviderType();
        this.password = password;
    }
}
