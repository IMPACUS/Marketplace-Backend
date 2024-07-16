package com.impacus.maketplace.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedSellerDTO {
    private String logoImageUrl;
    private String brandName;
    private String customerServiceNumber;
    private String representativeEmail;
    private String brandIntroduction;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String businessDay;
    private String breakingTime;
    private String email;
    private String phoneNumber;
    private SellerManagerInfoDTO manager;
    private SellerAdjustmentInfoDTO adjustment;
    private SellerDeliveryCompanyInfoDTO deliveryCompany;
    private List<SellerDeliveryAddressInfoDTO> deliveryAddress;
    private Long mainDeliveryAddressId;
}
