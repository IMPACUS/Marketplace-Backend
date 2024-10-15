package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentEventCoupon is a Querydsl query type for PaymentEventCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEventCoupon extends EntityPathBase<PaymentEventCoupon> {

    private static final long serialVersionUID = -1833496811L;

    public static final QPaymentEventCoupon paymentEventCoupon = new QPaymentEventCoupon("paymentEventCoupon");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final NumberPath<Long> paymentEventId = createNumber("paymentEventId", Long.class);

    public final NumberPath<Long> userCouponId = createNumber("userCouponId", Long.class);

    public QPaymentEventCoupon(String variable) {
        super(PaymentEventCoupon.class, forVariable(variable));
    }

    public QPaymentEventCoupon(Path<? extends PaymentEventCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentEventCoupon(PathMetadata metadata) {
        super(PaymentEventCoupon.class, metadata);
    }

}

