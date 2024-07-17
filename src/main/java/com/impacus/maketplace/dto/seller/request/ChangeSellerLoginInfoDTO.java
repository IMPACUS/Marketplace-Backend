package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeSellerLoginInfoDTO {
    @Valid
    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;

    @ValidPhoneNumber
    @NotBlank
    private String phoneNumber;
}
