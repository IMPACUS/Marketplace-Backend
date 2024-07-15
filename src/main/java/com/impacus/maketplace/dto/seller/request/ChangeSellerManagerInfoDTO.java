package com.impacus.maketplace.dto.seller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeSellerManagerInfoDTO {
    @NotBlank
    @Size(max = 25)
    private String representativeName;

    @NotBlank
    @Size(max = 25)
    private String address;

    @NotBlank
    @Size(max = 25)
    private String businessRegistrationNumber;

    @NotBlank
    @Size(max = 25)
    private String mailOrderBusinessReportNumber;
}
