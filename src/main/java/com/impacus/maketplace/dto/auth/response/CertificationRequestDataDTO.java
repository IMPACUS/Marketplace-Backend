package com.impacus.maketplace.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CertificationRequestDataDTO {
    private String encData;
    private String reqNumber;

    public static CertificationRequestDataDTO toDTO(
            String encData, String reqNumber
    ) {
        return new CertificationRequestDataDTO(encData, reqNumber);
    }
}
