package com.impacus.maketplace.entity.point;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRewardPoint is a Querydsl query type for RewardPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRewardPoint extends EntityPathBase<RewardPoint> {

    private static final long serialVersionUID = -1307192874L;

    public static final QRewardPoint rewardPoint = new QRewardPoint("rewardPoint");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final ComparablePath<java.time.Duration> expirationPeriod = createComparable("expirationPeriod", java.time.Duration.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.point.GrantMethod> grantMethod = createEnum("grantMethod", com.impacus.maketplace.common.enumType.point.GrantMethod.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> issueQuantity = createNumber("issueQuantity", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.point.RewardPointType> rewardPointType = createEnum("rewardPointType", com.impacus.maketplace.common.enumType.point.RewardPointType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.point.RewardPointStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.point.RewardPointStatus.class);

    public QRewardPoint(String variable) {
        super(RewardPoint.class, forVariable(variable));
    }

    public QRewardPoint(Path<? extends RewardPoint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRewardPoint(PathMetadata metadata) {
        super(RewardPoint.class, metadata);
    }

}

