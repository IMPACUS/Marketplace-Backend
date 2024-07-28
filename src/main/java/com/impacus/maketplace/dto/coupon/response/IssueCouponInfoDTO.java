package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueCouponInfoDTO {
    private Long couponId;
    private String name;
    private BenefitType benefitType;
    private Long benefitValue;
    private String description;
    private IssuedTimeType issuedTimeType;
    private ExpireTimeType expireTimeType;
    private Integer expireTimeDays;
    private StandardType useStandardType;
    private Long useStandardValue;
    private CoverageType useConverageType;
    private String useCoverageSubCategoryName;
    private Boolean smsAlarm;
    private Boolean emailAlarm;
    private Boolean kakaoAlarm;

    @QueryProjection
    public IssueCouponInfoDTO(Long couponId, String name, BenefitType benefitType, Long benefitValue, String description, IssuedTimeType issuedTimeType, ExpireTimeType expireTimeType, Integer expireTimeDays, StandardType useStandardType, Long useStandardValue, CoverageType useConverageType, String useCoverageSubCategoryName, Boolean smsAlarm, Boolean emailAlarm, Boolean kakaoAlarm) {
        this.couponId = couponId;
        this.name = name;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.description = description;
        this.issuedTimeType = issuedTimeType;
        this.expireTimeType = expireTimeType;
        this.expireTimeDays = expireTimeDays;
        this.useStandardType = useStandardType;
        this.useStandardValue = useStandardValue;
        this.useConverageType = useConverageType;
        this.useCoverageSubCategoryName = useCoverageSubCategoryName;
        this.smsAlarm = smsAlarm;
        this.emailAlarm = emailAlarm;
        this.kakaoAlarm = kakaoAlarm;
    }
}
