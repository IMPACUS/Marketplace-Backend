package com.impacus.maketplace.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrdersProductCoupons is a Querydsl query type for OrdersProductCoupons
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrdersProductCoupons extends EntityPathBase<OrdersProductCoupons> {

    private static final long serialVersionUID = 124303934L;

    public static final QOrdersProductCoupons ordersProductCoupons = new QOrdersProductCoupons("ordersProductCoupons");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ordersProductId = createNumber("ordersProductId", Long.class);

    public final NumberPath<Long> userCouponId = createNumber("userCouponId", Long.class);

    public QOrdersProductCoupons(String variable) {
        super(OrdersProductCoupons.class, forVariable(variable));
    }

    public QOrdersProductCoupons(Path<? extends OrdersProductCoupons> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrdersProductCoupons(PathMetadata metadata) {
        super(OrdersProductCoupons.class, metadata);
    }

}

