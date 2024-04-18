package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.entity.coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDetailDto {
    private String code;
    private String name;
    private String desc;
    private String couponBenefitClassification;
    private int benefitAmount;
    private String couponIssuanceClassification;
    private Long couponIssuanceClassificationData;
    private String couponPaymentTarget;
    private Long firstComeFirstServedAmount;
    private String couponIssuedTime;
    private String couponType;
    private String couponExpireTime;
    private Long expireDays;
    private String couponIssuanceCoverage;
    private String couponUseCoverage;
    private String couponUsableStandardAmountType;
    private int usableStandardMount;
    private String couponIssuanceStandardAmountType;
    private int issueStandardAmount;
    private String couponIssuancePeriod;
    private String startIssuanceAt;
    private String endIssuanceAt;
    private Long numberOfWithPeriod;
    private String couponIssuance;
    private String loginCouponIssueNotification;
    private String issuingCouponsSendSMS;
    private String issuanceCouponSendEmail;

    public static CouponDetailDto entityToDto(Coupon entity) {
        return CouponDetailDto.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .desc(entity.getDescription())
                .couponBenefitClassification(entity.getCouponBenefitClassification().getCode())
                .benefitAmount(entity.getBenefitAmount())
                .couponIssuanceClassification(entity.getCouponIssuanceClassification() != null ? entity.getCouponIssuance().getCode() : null)
                .couponIssuanceClassificationData(entity.getCouponIssuanceClassification() != null ? entity.getCouponIssuanceClassificationData().getId() : null)
                .couponPaymentTarget(entity.getCouponPaymentTarget().getCode())
                .firstComeFirstServedAmount(entity.getFirstComeFirstServedAmount())
                .couponIssuedTime(entity.getCouponIssuedTime().getCode())
                .couponType(entity.getCouponType().getCode())
                .couponExpireTime(entity.getCouponExpireTime().getCode())
                .expireDays(entity.getExpireDays())
                .couponIssuanceCoverage(entity.getCouponIssuanceCoverage().getCode())
                .couponUseCoverage(entity.getCouponUseCoverage().getCode())
                .couponUsableStandardAmountType(entity.getCouponUsableStandardAmountType().getCode())
                .usableStandardMount(entity.getUsableStandardAmount())
                .couponIssuanceStandardAmountType(entity.getCouponIssuanceStandardAmountType().getCode())
                .issueStandardAmount(entity.getIssueStandardAmount())
                .couponIssuancePeriod(entity.getCouponIssuancePeriod().getCode())
                .startIssuanceAt(entity.getStartIssuanceAt() != null ? entity.getStartIssuanceAt().toString() : null)
                .endIssuanceAt(entity.getEndIssuanceAt() != null ? entity.getEndIssuanceAt().toString() : null)
                .numberOfWithPeriod(entity.getNumberOfWithPeriod())
                .couponIssuance(entity.getCouponIssuance().getCode())
                .loginCouponIssueNotification(entity.getLoginCouponIssueNotification())
                .issuingCouponsSendSMS(entity.getIssuingCouponsSendSMS())
                .issuanceCouponSendEmail(entity.getIssuanceCouponSendEmail())
                .build();
    }


}
