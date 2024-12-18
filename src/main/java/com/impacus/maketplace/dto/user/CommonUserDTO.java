package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonUserDTO {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private UserType type;
    private UserStatus status;

    public OauthProviderType getOauthProviderType() {
        EmailInfoDTO dto = StringUtils.getEmailInfo(this.email);
        return dto.getOauthProviderType();
    }
}
