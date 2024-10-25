package com.impacus.maketplace.dto.coupon.api;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.api.QAlarmCouponDTO is a Querydsl Projection type for AlarmCouponDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAlarmCouponDTO extends ConstructorExpression<AlarmCouponDTO> {

    private static final long serialVersionUID = -568026569L;

    public QAlarmCouponDTO(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<String> phoneNumber, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> couponName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<java.time.LocalDate> expiredAt) {
        super(AlarmCouponDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, java.time.LocalDate.class}, userId, userName, phoneNumber, email, couponName, benefitType, benefitValue, expiredAt);
    }

}

