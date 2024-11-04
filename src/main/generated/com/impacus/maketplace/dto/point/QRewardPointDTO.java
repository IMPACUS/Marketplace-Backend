package com.impacus.maketplace.dto.point;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.point.QRewardPointDTO is a Querydsl Projection type for RewardPointDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRewardPointDTO extends ConstructorExpression<RewardPointDTO> {

    private static final long serialVersionUID = -871077271L;

    public QRewardPointDTO(com.querydsl.core.types.Expression<Long> rewardPointId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.RewardPointType> rewardPointType, com.querydsl.core.types.Expression<java.time.Duration> expirationPeriod, com.querydsl.core.types.Expression<Long> issueQuantity, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.GrantMethod> grantMethod, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.RewardPointStatus> status, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(RewardPointDTO.class, new Class<?>[]{long.class, com.impacus.maketplace.common.enumType.point.RewardPointType.class, java.time.Duration.class, long.class, com.impacus.maketplace.common.enumType.point.GrantMethod.class, com.impacus.maketplace.common.enumType.point.RewardPointStatus.class, java.time.LocalDateTime.class}, rewardPointId, rewardPointType, expirationPeriod, issueQuantity, grantMethod, status, createAt);
    }

}

