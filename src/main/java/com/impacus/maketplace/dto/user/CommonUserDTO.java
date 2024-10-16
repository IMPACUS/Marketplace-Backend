package com.impacus.maketplace.dto.user;

import com.impacus.maketplace.common.enumType.user.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonUserDTO {
    private Long userId;
    private String email;
    private String password;
    private UserType userType;
}
