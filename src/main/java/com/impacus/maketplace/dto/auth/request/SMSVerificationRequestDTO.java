package com.impacus.maketplace.dto.auth.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import lombok.Getter;

@Getter
public class SMSVerificationRequestDTO {
    @ValidPhoneNumber
    public String phoneNumber;
}
