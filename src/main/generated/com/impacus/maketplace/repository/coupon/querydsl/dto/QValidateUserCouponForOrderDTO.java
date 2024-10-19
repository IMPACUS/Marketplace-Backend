package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.coupon.querydsl.dto.QValidateUserCouponForOrderDTO is a Querydsl Projection type for ValidateUserCouponForOrderDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QValidateUserCouponForOrderDTO extends ConstructorExpression<ValidateUserCouponForOrderDTO> {

    private static final long serialVersionUID = 1619404045L;

    public QValidateUserCouponForOrderDTO(com.querydsl.core.types.Expression<Long> userCouponId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ProductType> productType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CoverageType> useCoverageType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.StandardType> useStandardType, com.querydsl.core.types.Expression<Long> useStandardValue) {
        super(ValidateUserCouponForOrderDTO.class, new Class<?>[]{long.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, com.impacus.maketplace.common.enumType.coupon.ProductType.class, com.impacus.maketplace.common.enumType.coupon.CoverageType.class, com.impacus.maketplace.common.enumType.coupon.StandardType.class, long.class}, userCouponId, benefitType, benefitValue, productType, useCoverageType, useStandardType, useStandardValue);
    }

}

