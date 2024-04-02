package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIssuedDto {

    private String code;

    @NotNull(message = "쿠폰 이름은 필수값 입니다.")
    private String name;
    @NotNull
    private String desc;
    @NotNull
    private String couponBenefitClassificationType;
    @NotNull
    private int benefitAmount;
    @NotNull
    private String couponIssuanceClassificationType;

    private Long couponIssuanceClassificationData;
    @NotNull
    private String couponPaymentTargetType;

    private Long firstComeFirstServedAmount;
    @NotNull
    private String couponIssuedTimeType;
    @NotNull
    private String couponType;
    @NotNull
    private String couponExpireTimeType;

    private Long expireDays;

    @NotNull
    private String couponIssuanceCoverageType;
    @NotNull
    private String couponUseCoverageType;

    @NotNull
    private String couponUsableStandardAmountType;

    @Builder.Default
    private int usableStandardMount = -1;

    @NotNull
    private String couponIssuanceStandardAmount;

    @Builder.Default
    private int issueStandardMount = -1;

    private String couponIssuancePeriodType;
    private String startIssuanceAt;
    private String endIssuanceAt;
    private Long numberOfWithPeriod = -1L;

    @NotNull
    private String couponIssuanceType;

    @Builder.Default
    private String loginCouponIssueNotification = "N";  // 로그인 쿠폰 발급 알림

    @Builder.Default
    @NotNull
    private String issuingCouponsSendSMS = "N"; // 쿠폰발급 SMS 발송
    @NotNull
    private String issuanceCouponSendEmail = "N";   // 쿠폰 발급 Email 발송

    private Boolean isActive = false;
}
