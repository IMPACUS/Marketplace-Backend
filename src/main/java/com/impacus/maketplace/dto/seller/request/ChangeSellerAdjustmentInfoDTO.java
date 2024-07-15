package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.BankCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeSellerAdjustmentInfoDTO {
    @ValidEnum(enumClass = BankCode.class)
    private BankCode bankCode;

    @NotBlank
    @Size(max = 25)
    private String accountName;

    @NotBlank
    @Size(max = 25)
    private String accountNumber;
}
