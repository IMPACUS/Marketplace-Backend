package com.impacus.maketplace.dto.seller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SellerDeliveryCompanyInfoDTO {
    private Integer generalDeliveryFee;
    private Integer generalSpecialDeliveryFee;
    private Integer refundDeliveryFee;
    private Integer refundSpecialDeliveryFee;
    private List<SelectedSellerDeliveryCompanyDTO> deliveryCompanies;
}
