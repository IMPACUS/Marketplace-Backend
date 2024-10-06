package com.impacus.maketplace.dto.coupon.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.response.QIssueCouponHIstoryDTO is a Querydsl Projection type for IssueCouponHIstoryDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QIssueCouponHIstoryDTO extends ConstructorExpression<IssueCouponHIstoryDTO> {

    private static final long serialVersionUID = 1159133432L;

    public QIssueCouponHIstoryDTO(com.querydsl.core.types.Expression<Long> couponId, com.querydsl.core.types.Expression<String> code, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> userEmail, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.UserCouponStatus> userCouponStatus, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.BenefitType> benefitType, com.querydsl.core.types.Expression<Long> benefitValue) {
        super(IssueCouponHIstoryDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, String.class, com.impacus.maketplace.common.enumType.coupon.UserCouponStatus.class, java.time.LocalDateTime.class, com.impacus.maketplace.common.enumType.coupon.BenefitType.class, long.class}, couponId, code, description, name, userEmail, userName, userCouponStatus, createAt, benefitType, benefitValue);
    }

}

