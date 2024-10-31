package com.impacus.maketplace.dto.coupon.response;


import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class CouponDetailDTO {

    private Long couponId;

    private String name;

    private String description;

    private BenefitType benefitType;

    private Long benefitValue;

    private TargetProductType productType;

    private PaymentTarget paymentTarget;

    private Integer firstCount;

    private IssuedTimeType issuedTimeType;

    private CouponType couponType;

    private CouponIssueType couponIssueType;

    private ExpireTimeType expireTimeType;

    private Integer expireTimeDays;

    private CoverageType issueCoverageType;

    private String issueCoverageSubCategoryName;

    private CoverageType useCoverageType;

    private String useCoverageSubCategoryName;

    private StandardType useStandardType;

    private Long useStandardValue;

    private StandardType issueConditionType;

    private Long issueConditionValue;

    private PeriodType periodType;

    private LocalDate periodStartAt;

    private LocalDate periodEndAt;

    private Long numberOfPeriod;

    private AutoManualType autoManualType;

    private String code;

    private Boolean loginAlarm;

    private Boolean smsAlarm;

    private Boolean emailAlarm;

    private Boolean kakaoAlarm;

    public static CouponDetailDTO fromEntity(Coupon coupon) {
        return CouponDetailDTO.builder()
                .couponId(coupon.getId())
                .code(coupon.getCode())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .benefitType(coupon.getBenefitType())
                .benefitValue(coupon.getBenefitValue())
                .productType(coupon.getProductType())
                .paymentTarget(coupon.getPaymentTarget())
                .firstCount(coupon.getFirstCount())
                .issuedTimeType(coupon.getIssuedTimeType())
                .couponIssueType(coupon.getCouponIssueType())
                .couponType(coupon.getCouponType())
                .expireTimeType(coupon.getExpireTimeType())
                .expireTimeDays(coupon.getExpireTimeDays())
                .issueCoverageType(coupon.getIssueCoverageType())
                .issueCoverageSubCategoryName(coupon.getIssueCoverageSubCategoryName())
                .useCoverageType(coupon.getUseCoverageType())
                .useCoverageSubCategoryName(coupon.getUseCoverageSubCategoryName())
                .useStandardType(coupon.getUseStandardType())
                .useStandardValue(coupon.getUseStandardValue())
                .issueConditionType(coupon.getIssueConditionType())
                .issueConditionValue(coupon.getIssueConditionValue())
                .periodType(coupon.getPeriodType())
                .periodStartAt(coupon.getPeriodStartAt())
                .periodEndAt(coupon.getPeriodEndAt())
                .numberOfPeriod(coupon.getNumberOfPeriod())
                .autoManualType(coupon.getAutoManualType())
                .loginAlarm(coupon.getLoginAlarm())
                .smsAlarm(coupon.getSmsAlarm())
                .kakaoAlarm(coupon.getKakaoAlarm())
                .build();
    }
}
