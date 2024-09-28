package com.impacus.maketplace.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountInfoDTO {
    private Long productId;
    private Long ecoDiscountAmount;          // 에코 할인 금액
    private Long productCouponDiscountAmount; // 개별 상품 쿠폰 할인 금액
    private Long orderCouponDiscountAmount;   // 주문 전체 쿠폰 할인 금액
    private Long pointDiscountAmount;         // 포인트 할인 금액
    private Long finalPrice;                  // 최종 결제 가격
}
