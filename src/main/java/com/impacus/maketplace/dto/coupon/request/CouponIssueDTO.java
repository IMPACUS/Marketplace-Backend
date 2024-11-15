package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueDTO implements CouponDTO {

    @NotNull(message = "쿠폰 이름은 필수값 입니다.")
    private String name;

    private String description;

    @ValidEnum(enumClass = BenefitType.class)
    private BenefitType benefitType;

    @NotNull
    private Long benefitValue;

    @ValidEnum(enumClass = CouponProductType.class)
    private CouponProductType productType;

    @ValidEnum(enumClass = PaymentTarget.class)
    private PaymentTarget paymentTarget;

    private Integer firstCount;

    @ValidEnum(enumClass = IssuedTimeType.class)
    private IssuedTimeType issuedTimeType;

    @ValidEnum(enumClass = CouponType.class)
    private CouponType couponType;

    private EventType eventType;

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
                .firstCount(this.paymentTarget == PaymentTarget.FIRST ? this.firstCount : null)
                .quantityIssued(0L)
                .issuedTimeType(this.issuedTimeType)
                .couponIssueType(this.couponIssueType)
                .couponType(this.couponType)
                .eventType(this.eventType)
                .expireTimeType(this.expireTimeType)
                .expireTimeDays(this.expireTimeType == ExpireTimeType.LIMIT ? this.expireTimeDays : null)
                .issueCoverageType(this.issueCoverageType)
                .issueCoverageSubCategoryName(this.issueCoverageType == CoverageType.BRAND ? this.issueCoverageSubCategoryName : null)
                .useCoverageType(this.useCoverageType)
                .useCoverageSubCategoryName(this.useCoverageType == CoverageType.BRAND ? this.useCoverageSubCategoryName : null)
                .useStandardType(this.useStandardType)
                .useStandardValue(this.useStandardType == StandardType.LIMIT ? this.useStandardValue : null)
                .issueConditionType(this.issueConditionType)
                .issueConditionValue(this.issueConditionType == StandardType.LIMIT ? this.issueConditionValue : null)
                .periodType(this.periodType)
                .periodStartAt(this.periodType == PeriodType.SET ? this.periodStartAt : null)
                .periodEndAt(this.periodType == PeriodType.SET ? this.periodEndAt : null)
                .numberOfPeriod(this.numberOfPeriod)
                .autoManualType(this.autoManualType)
                .loginAlarm(this.loginAlarm != null ? this.loginAlarm : false)
                .smsAlarm(this.smsAlarm != null ? this.smsAlarm : false)
                .emailAlarm(this.emailAlarm != null ? this.emailAlarm : false)
                .kakaoAlarm(this.kakaoAlarm != null ? this.kakaoAlarm : false)
                .statusType(CouponStatusType.ISSUING)
                .isDeleted(false)
                .build();
    }
}
