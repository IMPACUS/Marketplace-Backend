package com.impacus.maketplace.dto.user.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import lombok.Getter;

@Getter
public class UpdateUserDTO {
    
    @ValidEnum(enumClass = UserStatus.class)
    private UserStatus userStatus;
}
