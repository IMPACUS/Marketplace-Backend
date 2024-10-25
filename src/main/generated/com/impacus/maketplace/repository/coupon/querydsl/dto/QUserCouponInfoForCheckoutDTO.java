package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.coupon.querydsl.dto.QUserCouponInfoForCheckoutDTO is a Querydsl Projection type for UserCouponInfoForCheckoutDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUserCouponInfoForCheckoutDTO extends ConstructorExpression<UserCouponInfoForCheckoutDTO> {

    private static final long serialVersionUID = 166441845L;

    public QUserCouponInfoForCheckoutDTO(com.querydsl.core.types.Expression<Long> userCouponId, com.querydsl.core.types.Expression<String> couponName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ProductType> productType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CoverageType> useCoverageType, com.querydsl.core.types.Expression<String> useCoverageSubCategoryName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.StandardType> useStandardType, com.querydsl.core.types.Expression<Long> useStandardValue) {
        super(UserCouponInfoForCheckoutDTO.class, new Class<?>[]{long.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, com.impacus.maketplace.common.enumType.coupon.ProductType.class, com.impacus.maketplace.common.enumType.coupon.CoverageType.class, String.class, com.impacus.maketplace.common.enumType.coupon.StandardType.class, long.class}, userCouponId, couponName, benefitType, benefitValue, productType, useCoverageType, useCoverageSubCategoryName, useStandardType, useStandardValue);
    }

}

