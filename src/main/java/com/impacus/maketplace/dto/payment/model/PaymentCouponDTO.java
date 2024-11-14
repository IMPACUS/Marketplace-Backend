package com.impacus.maketplace.dto.payment.model;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import lombok.Data;

@Data
public class PaymentCouponDTO {
    private Long userCouponId;
    private BenefitType benefitType;    // 적용 방식
    private Long benefitValue;   // 혜택 금액 혹은 퍼센트

    public PaymentCouponDTO(Long userCouponId, BenefitType benefitType, Long benefitValue) {
        this.userCouponId = userCouponId;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
    }
}
