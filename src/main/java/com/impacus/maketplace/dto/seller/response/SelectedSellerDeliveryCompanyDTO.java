package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedSellerDeliveryCompanyDTO {
    private Long selectedSellerDeliveryCompanyId;
    private DeliveryCompany deliveryCompany;
    
    public boolean isNull() {
        return selectedSellerDeliveryCompanyId == null && deliveryCompany == null;
    }
}
