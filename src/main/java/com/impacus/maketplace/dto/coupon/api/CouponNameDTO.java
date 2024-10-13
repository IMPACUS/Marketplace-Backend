package com.impacus.maketplace.dto.coupon.api;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CouponNameDTO {
    private Long couponId;
    private String couponName;
    private BenefitType benefitType;
    private Long benefitValue;

    @QueryProjection
    public CouponNameDTO(Long couponId, String couponName, BenefitType benefitType, Long benefitValue) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
    }
}
