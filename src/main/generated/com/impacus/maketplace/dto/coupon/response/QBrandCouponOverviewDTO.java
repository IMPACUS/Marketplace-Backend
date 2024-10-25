package com.impacus.maketplace.dto.coupon.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.response.QBrandCouponOverviewDTO is a Querydsl Projection type for BrandCouponOverviewDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBrandCouponOverviewDTO extends ConstructorExpression<BrandCouponOverviewDTO> {

    private static final long serialVersionUID = 1870786027L;

    public QBrandCouponOverviewDTO(com.querydsl.core.types.Expression<Long> couponId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.PeriodType> periodType, com.querydsl.core.types.Expression<java.time.LocalDate> periodStartAt, com.querydsl.core.types.Expression<java.time.LocalDate> periodEndAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ProductType> productType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CoverageType> useCoverageType, com.querydsl.core.types.Expression<String> useCoverageSubCategoryName) {
        super(BrandCouponOverviewDTO.class, new Class<?>[]{long.class, String.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, com.impacus.maketplace.common.enumType.coupon.PeriodType.class, java.time.LocalDate.class, java.time.LocalDate.class, com.impacus.maketplace.common.enumType.coupon.ProductType.class, com.impacus.maketplace.common.enumType.coupon.CoverageType.class, String.class}, couponId, name, description, benefitType, benefitValue, periodType, periodStartAt, periodEndAt, productType, useCoverageType, useCoverageSubCategoryName);
    }

}

