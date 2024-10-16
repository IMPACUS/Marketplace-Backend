package com.impacus.maketplace.dto.coupon.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.coupon.response.QCouponListInfoDTO is a Querydsl Projection type for CouponListInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCouponListInfoDTO extends ConstructorExpression<CouponListInfoDTO> {

    private static final long serialVersionUID = 450908027L;

    public QCouponListInfoDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> code, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.StandardType> issueConditionType, com.querydsl.core.types.Expression<Long> issueConditionValue, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.ExpireTimeType> expireTimeType, com.querydsl.core.types.Expression<Integer> expireTimeDays, com.querydsl.core.types.Expression<Long> quantityIssued, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.AutoManualType> autoManualType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.coupon.CouponStatusType> statusType, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifyAt) {
        super(CouponListInfoDTO.class, new Class<?>[]{long.class, String.class, String.class, com.impacus.maketplace.common.enumType.coupon.StandardType.class, long.class, com.impacus.maketplace.common.enumType.coupon.ExpireTimeType.class, int.class, long.class, com.impacus.maketplace.common.enumType.coupon.AutoManualType.class, com.impacus.maketplace.common.enumType.coupon.CouponStatusType.class, java.time.LocalDateTime.class}, id, code, name, issueConditionType, issueConditionValue, expireTimeType, expireTimeDays, quantityIssued, autoManualType, statusType, modifyAt);
    }

}

