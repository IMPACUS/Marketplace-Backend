package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.coupon.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIssuedDto {

    private String code;
    private String name;
    private String desc;
    private String couponBenefitClassificationType;
    private BigDecimal benefitAmount;
    private String couponIssuanceClassificationType;
    private String couponPaymentTargetType;
    private Long firstComeFirstServedAmount;
    private String couponIssuedTimeType;
    private String couponExpireTimeType;
    private Long expireDays;
    private String couponIssuanceCoverageType;
    private String couponUseCoverageType;

    private String couponUsableStandardAmountType;
    private BigDecimal usableStandardMount;

    private String couponIssuanceStandardAmount;
    private BigDecimal issueStandardMount;

    private String paymentMethodType;

    private String couponIssuancePeriodType;
    private String startIssuanceAt;
    private String endIssuanceAt;
    private Long numberOfWithPeriod;
    private String couponIssuanceType;

    private String loginCouponIssueNotification = "N";  // 로그인 쿠폰 발급 알림

    private String issuingCouponsSendSMS = "N"; // 쿠폰발급 SMS 발송

    private String issuanceCouponSendEmail = "N";   // 쿠폰 발급 Email 발송

    private Boolean isActive = false;

//    public CouponBenefitClassification getCouponTypeEnum() {
//        return CouponBenefitClassification.fromCode(this.couponType);
//    }

}
