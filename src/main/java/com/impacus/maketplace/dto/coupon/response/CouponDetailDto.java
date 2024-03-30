package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.CouponCoverage;
import com.impacus.maketplace.common.enumType.coupon.CouponExpireTime;
import com.impacus.maketplace.common.enumType.coupon.CouponIssuedTime;
import com.impacus.maketplace.common.enumType.coupon.CouponStandardAmountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDetailDto {
    private Long id;
    private String name;
    private String couponBenefitClassification;
    private String benefitAmount;
    private String description;
    private CouponIssuedTime couponIssuedTime;
    private CouponExpireTime couponExpireTime;
    private Long expireDays;
    private CouponStandardAmountType couponUsableStandardAmount;
    private int usableStandardMount;
    private CouponCoverage couponUseCoverage;



}
