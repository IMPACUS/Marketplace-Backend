package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateSellerInfoFromAdminDTO {
    @ValidEnum(enumClass = UserStatus.class)
    private UserStatus userStatus;

    @NotNull
    @Min(0)
    @Max(100)
    private int charge;
}
