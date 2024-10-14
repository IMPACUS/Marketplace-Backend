package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.coupon.querydsl.dto.QValidateUserCouponForProductDTO is a Querydsl Projection type for ValidateUserCouponForProductDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QValidateUserCouponForProductDTO extends ConstructorExpression<ValidateUserCouponForProductDTO> {

    private static final long serialVersionUID = -1803351860L;

    public QValidateUserCouponForProductDTO(com.querydsl.core.types.Expression<Long> userCouponId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ProductType> productType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CoverageType> useCoverageType, com.querydsl.core.types.Expression<String> useCoverageSubCategoryName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.StandardType> useStandardType, com.querydsl.core.types.Expression<Long> useStandardValue) {
        super(ValidateUserCouponForProductDTO.class, new Class<?>[]{long.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, com.impacus.maketplace.common.enumType.coupon.ProductType.class, com.impacus.maketplace.common.enumType.coupon.CoverageType.class, String.class, com.impacus.maketplace.common.enumType.coupon.StandardType.class, long.class}, userCouponId, benefitType, benefitValue, productType, useCoverageType, useCoverageSubCategoryName, useStandardType, useStandardValue);
    }

}

