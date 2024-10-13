package com.impacus.maketplace.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductPricingDTO {
    private Long productId;
    private Long appSalesPrice;       // 앱 판매가
    private Long ecoDiscountAmount;   // 에코 할인 금액
    private Long priceAfterEcoDiscount; // 에코 할인 적용 후 가격

    @Builder
    ProductPricingDTO(Long productId, Long appSalesPrice, Long ecoDiscountAmount) {
        this.productId = productId;
        this.appSalesPrice = appSalesPrice;
        this.ecoDiscountAmount = ecoDiscountAmount;
        this.priceAfterEcoDiscount = appSalesPrice - ecoDiscountAmount;
    }
}
