package com.impacus.maketplace.dto.coupon.api;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.api.QCouponNameDTO is a Querydsl Projection type for CouponNameDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCouponNameDTO extends ConstructorExpression<CouponNameDTO> {

    private static final long serialVersionUID = 1680868543L;

    public QCouponNameDTO(com.querydsl.core.types.Expression<Long> couponId, com.querydsl.core.types.Expression<String> couponName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue) {
        super(CouponNameDTO.class, new Class<?>[]{long.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class}, couponId, couponName, benefitType, benefitValue);
    }

}

