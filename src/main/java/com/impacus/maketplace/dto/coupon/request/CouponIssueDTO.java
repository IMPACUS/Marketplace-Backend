package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIssueDTO implements CouponDTO {

    @NotNull(message = "쿠폰 이름은 필수값 입니다.")
    private String name;

    private String description;

    @ValidEnum(enumClass = BenefitType.class)
    private BenefitType benefitType;

    @NotNull
    private Long benefitValue;

    @ValidEnum(enumClass = ProductType.class)
    private ProductType productType;

    @ValidEnum(enumClass = PaymentTarget.class)
    private PaymentTarget paymentTarget;

    private Integer firstCount;

    @ValidEnum(enumClass = IssuedTimeType.class)
    private IssuedTimeType issuedTimeType;

    @ValidEnum(enumClass = CouponType.class)
    private CouponType couponType;

    @ValidEnum(enumClass = CouponIssueType.class)
    private CouponIssueType couponIssueType;

    @ValidEnum(enumClass = ExpireTimeType.class)
    private ExpireTimeType expireTimeType;

    private Integer expireTimeDays;

    @ValidEnum(enumClass = CoverageType.class)
    private CoverageType issueCoverageType;

    private String issueCoverageSubCategoryName;

    @ValidEnum(enumClass = CoverageType.class)
    private CoverageType useCoverageType;

    private String useCoverageSubCategoryName;

    @ValidEnum(enumClass = StandardType.class)
    private StandardType useStandardType;

    private Long useStandardValue;

    @ValidEnum(enumClass = StandardType.class)
    private StandardType issueConditionType;

    private Long issueConditionValue;

    @ValidEnum(enumClass = PeriodType.class)
    private PeriodType periodType;

    private LocalDate periodStartAt;

    private LocalDate periodEndAt;

    private Long numberOfPeriod;

    @ValidEnum(enumClass = AutoManualType.class)
    private AutoManualType autoManualType;

    private String code;

    @NotNull
    private Boolean loginAlarm;

    @NotNull
    private Boolean smsAlarm;

    @NotNull
    private Boolean emailAlarm;

    @NotNull
    private Boolean kakaoAlarm;

    public Coupon toEntity(String code) {
        return Coupon.builder()
                .code(code)
                .name(this.name)
                .description(this.description)
                .benefitType(this.benefitType)
                .benefitValue(this.benefitValue)
                .productType(this.productType)
                .paymentTarget(this.paymentTarget)
                .firstCount(this.firstCount)
                .quantityIssued(0L)
                .issuedTimeType(this.issuedTimeType)
                .couponIssueType(this.couponIssueType)
                .couponType(this.couponType)
                .expireTimeType(this.expireTimeType)
                .expireTimeDays(this.expireTimeDays)
                .issueCoverageType(this.issueCoverageType)
                .issueCoverageSubCategoryName(this.issueCoverageSubCategoryName)
                .useCoverageType(this.useCoverageType)
                .useCoverageSubCategoryName(this.useCoverageSubCategoryName)
                .useStandardType(this.useStandardType)
                .useStandardValue(this.useStandardValue)
                .issueConditionType(this.issueConditionType)
                .issueConditionValue(this.issueConditionValue)
                .periodType(this.periodType)
                .periodStartAt(this.periodStartAt)
                .periodEndAt(this.periodEndAt)
                .numberOfPeriod(this.numberOfPeriod)
                .autoManualType(this.autoManualType)
                .loginAlarm(this.loginAlarm)
                .smsAlarm(this.smsAlarm)
                .emailAlarm(this.emailAlarm)
                .kakaoAlarm(this.kakaoAlarm)
                .statusType(CouponStatusType.ISSUING)
                .isDeleted(false)
                .build();
    }
}
