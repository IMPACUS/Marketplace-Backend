package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponTrigger is a Querydsl query type for CouponTrigger
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponTrigger extends EntityPathBase<CouponTrigger> {

    private static final long serialVersionUID = 778357815L;

    public static final QCouponTrigger couponTrigger = new QCouponTrigger("couponTrigger");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> issuedCouponHistoryId = createNumber("issuedCouponHistoryId", Long.class);

    public final NumberPath<Long> triggerId = createNumber("triggerId", Long.class);

    public QCouponTrigger(String variable) {
        super(CouponTrigger.class, forVariable(variable));
    }

    public QCouponTrigger(Path<? extends CouponTrigger> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponTrigger(PathMetadata metadata) {
        super(CouponTrigger.class, metadata);
    }

}

