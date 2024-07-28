package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.ExpireTimeType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCouponOverviewDTO {
    private Long couponId;
    private String name;
    private String description;
    private BenefitType benefitType;
    private Long benefitValue;
    private ExpireTimeType expireTimeType;
    //private LocalDate localDate; ? Time?
}
