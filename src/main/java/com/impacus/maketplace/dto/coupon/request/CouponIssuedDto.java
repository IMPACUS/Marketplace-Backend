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
    private String description;
    @NotNull
    private String benefitType;
    @NotNull
    private int benefitValue;

    @NotNull
    private String productTargetType;


    @NotNull
    private String couponPaymentTargetType;

    private Long firstCount;

    @NotNull
    private String issuedTimeType;

    @NotNull
    private String type;

    @NotNull
    private String expireTimeType;

    private Long expireDays;

    @NotNull
    private String issueCoverageType;
    @NotNull
    private String useCoverageType;

    @NotNull
    private String useStandardType;

    @Builder.Default
    private Integer useStandardValue = -1;

    @NotNull
    private String issueStandardType;

    @Builder.Default
    private Integer issueStandardValue = -1;

    private String periodType;
    private String periodStartAt;
    private String periodEndAt;
    private Long numberOfPeriod = -1L;

    @NotNull
    private String autoManualType;

    @Builder.Default
    private String loginAlert = "N";  // 로그인 쿠폰 발급 알림

    @Builder.Default
    @NotNull
    private String smsAlert = "N"; // 쿠폰발급 SMS 발송

    @Builder.Default
    @NotNull
    private String emailAlert = "N";   // 쿠폰 발급 Email 발송

    private Boolean isActive = false;
}
