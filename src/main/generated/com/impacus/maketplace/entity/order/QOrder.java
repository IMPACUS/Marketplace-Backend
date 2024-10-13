package com.impacus.maketplace.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1669239937L;

    public static final QOrder order = new QOrder("order1");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath orderNumber = createString("orderNumber");

    public final EnumPath<com.impacus.maketplace.common.enumType.order.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.impacus.maketplace.common.enumType.order.PaymentStatus.class);

    public final NumberPath<Integer> productCount = createNumber("productCount", Integer.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> totalCommissionFee = createNumber("totalCommissionFee", Long.class);

    public final NumberPath<Long> totalCouponDiscount = createNumber("totalCouponDiscount", Long.class);

    public final NumberPath<Long> totalDeliveryFee = createNumber("totalDeliveryFee", Long.class);

    public final NumberPath<Long> totalEcoDiscount = createNumber("totalEcoDiscount", Long.class);

    public final NumberPath<Long> totalGreenLabelPoints = createNumber("totalGreenLabelPoints", Long.class);

    public final NumberPath<Long> totalOrderAmount = createNumber("totalOrderAmount", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

