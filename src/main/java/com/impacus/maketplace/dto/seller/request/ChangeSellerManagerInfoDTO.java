package com.impacus.maketplace.dto.seller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeSellerManagerInfoDTO {
    @NotBlank
    private String representativeName;

    @NotBlank
    private String address;

    @NotBlank
    private String businessRegistrationNumber;

    private String mailOrderBusinessReportNumber;
}
