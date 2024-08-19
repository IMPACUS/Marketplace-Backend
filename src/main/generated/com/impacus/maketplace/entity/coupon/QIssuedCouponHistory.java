package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIssuedCouponHistory is a Querydsl query type for IssuedCouponHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIssuedCouponHistory extends EntityPathBase<IssuedCouponHistory> {

    private static final long serialVersionUID = -165212568L;

    public static final QIssuedCouponHistory issuedCouponHistory = new QIssuedCouponHistory("issuedCouponHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> issuedAt = createDate("issuedAt", java.time.LocalDate.class);

    public final NumberPath<Long> issuedCouponId = createNumber("issuedCouponId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.coupon.TriggerType> triggerType = createEnum("triggerType", com.impacus.maketplace.common.enumType.coupon.TriggerType.class);

    public QIssuedCouponHistory(String variable) {
        super(IssuedCouponHistory.class, forVariable(variable));
    }

    public QIssuedCouponHistory(Path<? extends IssuedCouponHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIssuedCouponHistory(PathMetadata metadata) {
        super(IssuedCouponHistory.class, metadata);
    }

}

