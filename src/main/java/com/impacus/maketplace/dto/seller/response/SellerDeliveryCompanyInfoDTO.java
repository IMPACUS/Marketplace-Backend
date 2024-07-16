package com.impacus.maketplace.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDeliveryCompanyInfoDTO {
    private int generalDeliveryFee;
    private int generalSpecialDeliveryFee;
    private int refundDeliveryFee;
    private int refundSpecialDeliveryFee;
    private List<SelectedSellerDeliveryCompanyDTO> deliveryCompanies;
}
