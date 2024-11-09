package com.impacus.maketplace.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.impacus.maketplace.dto.payment.model.PaymentProductInfoIdDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentProductInfoDTO {
    Long productId;     // 결제 상품의 id
    Long productOptionId; // 결제 상품의 option id
    Long quantity;  // 결제 상품 수량
    Long sellerId;  // 판매자 id
    List<Long> appliedProductCouponIds = new ArrayList<>();    // 결제 상품에 적용된 쿠폰 리스트

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setAppliedProductCouponIds(List<Long> appliedProductCouponIds) {
        this.appliedProductCouponIds = appliedProductCouponIds != null ? appliedProductCouponIds : Collections.emptyList();
    }

    public PaymentProductInfoIdDTO getPaymentProductInfoId() {
        return new PaymentProductInfoIdDTO(this.getProductId(), this.getProductOptionId(), this.getSellerId());
    }
}
