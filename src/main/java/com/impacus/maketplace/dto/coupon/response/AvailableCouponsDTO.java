package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import lombok.Data;

@Data
public class AvailableCouponsDTO {
    private Long userCouponId;  // 사용자 쿠폰 id
    private String couponName;    // 쿠폰 이름
    private BenefitType benefitType;  // 혜택 구분 [ 원, % ]
    private Long benefitValue;   // 혜택 금액 혹은 퍼센트

    public AvailableCouponsDTO(Long userCouponId, String couponName, BenefitType benefitType, Long benefitValue) {
        this.userCouponId = userCouponId;
        this.couponName = couponName;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
    }
}
