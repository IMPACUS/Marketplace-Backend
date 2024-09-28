package com.impacus.maketplace.dto.payment;

import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckoutCartProductInfoDTO {
    private CheckoutProductInfoDTO checkoutProductInfoDTO;
    private Long quantity;
    List<Long> appliedCouponForProductIds;    // 결제 상품에 적용된 쿠폰 리스트
}
