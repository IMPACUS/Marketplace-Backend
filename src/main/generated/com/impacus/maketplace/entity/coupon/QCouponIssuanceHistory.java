package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponIssuanceHistory is a Querydsl query type for CouponIssuanceHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponIssuanceHistory extends EntityPathBase<CouponIssuanceHistory> {

    private static final long serialVersionUID = -1353211080L;

    public static final QCouponIssuanceHistory couponIssuanceHistory = new QCouponIssuanceHistory("couponIssuanceHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.TriggerType> triggerType = createEnum("triggerType", com.impacus.maketplace.common.enumType.coupon.TriggerType.class);

    public final NumberPath<Long> userCouponId = createNumber("userCouponId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCouponIssuanceHistory(String variable) {
        super(CouponIssuanceHistory.class, forVariable(variable));
    }

    public QCouponIssuanceHistory(Path<? extends CouponIssuanceHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponIssuanceHistory(PathMetadata metadata) {
        super(CouponIssuanceHistory.class, metadata);
    }

}

