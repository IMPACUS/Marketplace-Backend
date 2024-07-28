package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.entity.coupon.Coupon;
import jakarta.validation.constraints.Min;
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
public class CouponUpdateDTO implements CouponDTO {

    @NotNull(message = "쿠폰 id는 필수값 입니다.")
    private Long couponId;

    @NotNull(message = "쿠폰 이름은 필수값 입니다.")
    private String name;

    private String description;

    @ValidEnum(enumClass = BenefitType.class)
    private BenefitType benefitType;

    @NotNull
    @Min(value = 0, message = "음수 입력은 허용되지 않습니다.")
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

    // Null일 경우, 자동 발급
    private String code;

    @NotNull
    private Boolean loginAlarm;

    @NotNull
    private Boolean smsAlarm;

    @NotNull
    private Boolean emailAlarm;

    @NotNull
    private Boolean kakaoAlarm;
}
