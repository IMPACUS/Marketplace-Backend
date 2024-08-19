package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIssuedCouponTriggers is a Querydsl query type for IssuedCouponTriggers
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIssuedCouponTriggers extends EntityPathBase<IssuedCouponTriggers> {

    private static final long serialVersionUID = 2006282855L;

    public static final QIssuedCouponTriggers issuedCouponTriggers = new QIssuedCouponTriggers("issuedCouponTriggers");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> issuedCouponHistoryId = createNumber("issuedCouponHistoryId", Long.class);

    public final NumberPath<Long> triggerId = createNumber("triggerId", Long.class);

    public QIssuedCouponTriggers(String variable) {
        super(IssuedCouponTriggers.class, forVariable(variable));
    }

    public QIssuedCouponTriggers(Path<? extends IssuedCouponTriggers> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIssuedCouponTriggers(PathMetadata metadata) {
        super(IssuedCouponTriggers.class, metadata);
    }

}

