package com.impacus.maketplace.dto.seller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerManagerInfoDTO {
    private String representativeName;
    private String address;
    private String businessRegistrationNumber;
    private String mailOrderBusinessReportNumber;
    private String businessRegistrationUrl;
    private String mailOrderBusinessReportUrl;
}
