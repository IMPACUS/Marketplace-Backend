package com.impacus.maketplace.dto.auth.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SMSVerificationForEmailDTO {
    @ValidPhoneNumber
    public String phoneNumber;

    @NotBlank
    private String code;
}
