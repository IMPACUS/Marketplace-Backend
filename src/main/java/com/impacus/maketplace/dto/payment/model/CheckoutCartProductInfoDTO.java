package com.impacus.maketplace.dto.payment.model;

import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutCartProductInfoDTO {
    private CheckoutProductInfoDTO checkoutProductInfoDTO;
    private Long quantity;
    List<Long> appliedCouponForProductIds;    // 결제 상품에 적용된 쿠폰 리스트

    public CheckoutCartProductInfoDTO(CheckoutProductInfoDTO checkoutProductInfoDTO, Long quantity, List<Long> appliedCouponForProductIds) {
        this.checkoutProductInfoDTO = checkoutProductInfoDTO;
        this.quantity = quantity;
        this.appliedCouponForProductIds = appliedCouponForProductIds;
    }
}
