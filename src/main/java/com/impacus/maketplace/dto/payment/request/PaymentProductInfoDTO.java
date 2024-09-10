package com.impacus.maketplace.dto.payment.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentProductInfoDTO {
    Long productId;     // 결제 상품의 id
    Long productOptionId; // 결제 상품의 option id
    Long quantity;  // 결제 상품 수량
    Long sellerId;  // 판매자 id
    List<Long> applieCouponForProductIds;    // 결제 상품에 적용된 쿠폰 리스트
}
