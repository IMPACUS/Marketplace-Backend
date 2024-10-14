package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentOrderCoupon is a Querydsl query type for PaymentOrderCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOrderCoupon extends EntityPathBase<PaymentOrderCoupon> {

    private static final long serialVersionUID = -310063927L;

    public static final QPaymentOrderCoupon paymentOrderCoupon = new QPaymentOrderCoupon("paymentOrderCoupon");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final NumberPath<Long> paymentOrderId = createNumber("paymentOrderId", Long.class);

    public final NumberPath<Long> userCouponId = createNumber("userCouponId", Long.class);

    public QPaymentOrderCoupon(String variable) {
        super(PaymentOrderCoupon.class, forVariable(variable));
    }

    public QPaymentOrderCoupon(Path<? extends PaymentOrderCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentOrderCoupon(PathMetadata metadata) {
        super(PaymentOrderCoupon.class, metadata);
    }

}

