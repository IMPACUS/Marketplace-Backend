package com.impacus.maketplace.dto.coupon.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.response.QUserCouponOverviewDTO is a Querydsl Projection type for UserCouponOverviewDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUserCouponOverviewDTO extends ConstructorExpression<UserCouponOverviewDTO> {

    private static final long serialVersionUID = 625734083L;

    public QUserCouponOverviewDTO(com.querydsl.core.types.Expression<Long> userCouponId, com.querydsl.core.types.Expression<Long> couponId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue, com.querydsl.core.types.Expression<Boolean> isDownload, com.querydsl.core.types.Expression<java.time.LocalDateTime> downloadAt, com.querydsl.core.types.Expression<Boolean> isUsed, com.querydsl.core.types.Expression<java.time.LocalDateTime> usedAt, com.querydsl.core.types.Expression<java.time.LocalDate> expiredAt, com.querydsl.core.types.Expression<java.time.LocalDate> availableDownloadAt) {
        super(UserCouponOverviewDTO.class, new Class<?>[]{long.class, long.class, String.class, String.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class, boolean.class, java.time.LocalDateTime.class, boolean.class, java.time.LocalDateTime.class, java.time.LocalDate.class, java.time.LocalDate.class}, userCouponId, couponId, name, description, benefitType, benefitValue, isDownload, downloadAt, isUsed, usedAt, expiredAt, availableDownloadAt);
    }

}

