package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class IssueCouponStatisticDTO {
    private BenefitType benefitType;
    private Long count;
    private Long totalBenefitValue;

    @QueryProjection
    public IssueCouponStatisticDTO(BenefitType benefitType, Long count, Long totalBenefitValue) {
        this.benefitType = benefitType;
        this.count = count;
        this.totalBenefitValue = totalBenefitValue;
    }
}
