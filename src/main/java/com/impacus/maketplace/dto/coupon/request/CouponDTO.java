package com.impacus.maketplace.dto.coupon.request;

import com.impacus.maketplace.common.enumType.coupon.*;

import java.time.LocalDate;

public interface CouponDTO {
    String getCode();
    BenefitType getBenefitType();
    Long getBenefitValue();
    PaymentTarget getPaymentTarget();
    Integer getFirstCount();
    ExpireTimeType getExpireTimeType();
    Integer getExpireTimeDays();
    CoverageType getIssueCoverageType();
    String getIssueCoverageSubCategoryName();
    CoverageType getUseCoverageType();
    String getUseCoverageSubCategoryName();
    StandardType getUseStandardType();
    Long getUseStandardValue();
    StandardType getIssueConditionType();
    Long getIssueConditionValue();
    PeriodType getPeriodType();
    LocalDate getPeriodStartAt();
    LocalDate getPeriodEndAt();
    Long getNumberOfPeriod();
}
