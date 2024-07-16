package com.impacus.maketplace.dto.seller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SellerManagerInfoDTO {
    private String representativeName;
    private String address;
    private String businessRegistrationNumber;
    private String mailOrderBusinessReportNumber;
    private String businessRegistrationUrl;
    private String mailOrderBusinessReportUrl;
}
