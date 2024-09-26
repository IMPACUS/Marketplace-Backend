package com.impacus.maketplace.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPricingDTO {
    private Long productId;
    private Long appSalesPrice;       // 앱 판매가
    private Long ecoDiscountAmount;   // 에코 할인 금액
    private Long priceAfterEcoDiscount; // 에코 할인 적용 후 가격
}
