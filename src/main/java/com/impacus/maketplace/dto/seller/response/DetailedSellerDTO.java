package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
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

    public DetailedSellerDTO(
            String logoImageUrl,
            String brandName,
            String customerServiceNumber,
            String representativeEmail,
            String brandIntroduction,
            LocalTime openingTime,
            LocalTime closingTime,
            String businessDay,
            String breakingTime,
            String email,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            SellerManagerInfoDTO manager,
            SellerAdjustmentInfoDTO adjustment,
            SellerDeliveryCompanyInfoDTO deliveryCompany,
            List<SellerDeliveryAddressInfoDTO> deliveryAddress,
            Long mainDeliveryAddressId
    ) {
        this.logoImageUrl = logoImageUrl;
        this.brandName = brandName;
        this.customerServiceNumber = customerServiceNumber;
        this.representativeEmail = representativeEmail;
        this.brandIntroduction = brandIntroduction;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.businessDay = businessDay;
        this.breakingTime = breakingTime;
        this.email = email;
        this.phoneNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.manager = manager;
        this.adjustment = adjustment;
        this.deliveryCompany = deliveryCompany;
        this.deliveryAddress = deliveryAddress;
        this.mainDeliveryAddressId = mainDeliveryAddressId;
    }
}
