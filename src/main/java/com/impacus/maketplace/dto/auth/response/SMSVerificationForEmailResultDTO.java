package com.impacus.maketplace.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SMSVerificationForEmailResultDTO {
    private boolean result;
    private String email;

    public static SMSVerificationForEmailResultDTO toDTO(
            boolean result, String email
    ) {
        return new SMSVerificationForEmailResultDTO(result, email);
    }

    public static SMSVerificationForEmailResultDTO toDTO(
            boolean result
    ) {
        return new SMSVerificationForEmailResultDTO(result, null);
    }
}
