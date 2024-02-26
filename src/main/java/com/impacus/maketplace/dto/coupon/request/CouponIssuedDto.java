package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.enumType.coupon.CouponBenefitClassification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIssuedDto {

    private String expiredAt;

    private Integer discount;

    private String couponType;  // 쿠폰 타입 : [ 금액, 퍼센트 ]

    private Integer constraints;

    public CouponBenefitClassification getCouponTypeEnum() {
        return CouponBenefitClassification.fromCode(this.couponType);
    }

}
