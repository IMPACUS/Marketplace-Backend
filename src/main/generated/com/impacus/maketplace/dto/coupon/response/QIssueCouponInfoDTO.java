package com.impacus.maketplace.dto.coupon.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.response.QIssueCouponInfoDTO is a Querydsl Projection type for IssueCouponInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QIssueCouponInfoDTO extends ConstructorExpression<IssueCouponInfoDTO> {

    private static final long serialVersionUID = -335187708L;

    public QIssueCouponInfoDTO(com.querydsl.core.types.Expression<Long> couponId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.IssuedTimeType> issuedTimeType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ExpireTimeType> expireTimeType, com.querydsl.core.types.Expression<Integer> expireTimeDays, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.StandardType> useStandardType, com.querydsl.core.types.Expression<Long> useStandardValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CoverageType> useConverageType, com.querydsl.core.types.Expression<String> useCoverageSubCategoryName, com.querydsl.core.types.Expression<Boolean> smsAlarm, com.querydsl.core.types.Expression<Boolean> emailAlarm, com.querydsl.core.types.Expression<Boolean> kakaoAlarm) {
        super(IssueCouponInfoDTO.class, new Class<?>[]{long.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, String.class, com.impacus.maketplace.common.enumType.coupon.IssuedTimeType.class, com.impacus.maketplace.common.enumType.coupon.ExpireTimeType.class, int.class, com.impacus.maketplace.common.enumType.coupon.StandardType.class, long.class, com.impacus.maketplace.common.enumType.coupon.CoverageType.class, String.class, boolean.class, boolean.class, boolean.class}, couponId, name, benefitType, benefitValue, description, issuedTimeType, expireTimeType, expireTimeDays, useStandardType, useStandardValue, useConverageType, useCoverageSubCategoryName, smsAlarm, emailAlarm, kakaoAlarm);
    }

}

