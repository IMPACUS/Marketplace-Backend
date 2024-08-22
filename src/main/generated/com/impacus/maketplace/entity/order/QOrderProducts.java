package com.impacus.maketplace.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderProducts is a Querydsl query type for OrderProducts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderProducts extends EntityPathBase<OrderProducts> {

    private static final long serialVersionUID = 338475269L;

    public static final QOrderProducts orderProducts = new QOrderProducts("orderProducts");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Integer> commissionPercent = createNumber("commissionPercent", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> ecoDiscountAmount = createNumber("ecoDiscountAmount", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.DiscountType> ecoDiscountType = createEnum("ecoDiscountType", com.impacus.maketplace.common.enumType.DiscountType.class);

    public final NumberPath<Integer> exchangeQuantity = createNumber("exchangeQuantity", Integer.class);

    public final NumberPath<Long> greenLabelDiscount = createNumber("greenLabelDiscount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> individualCouponDiscount = createNumber("individualCouponDiscount", Long.class);

    public final BooleanPath isEcoProduct = createBoolean("isEcoProduct");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> ordersId = createNumber("ordersId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.order.OrderStatus> orderStatus = createEnum("orderStatus", com.impacus.maketplace.common.enumType.order.OrderStatus.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> productOptionHistoryId = createNumber("productOptionHistoryId", Long.class);

    public final NumberPath<Long> productPrice = createNumber("productPrice", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Integer> returnQuantity = createNumber("returnQuantity", Integer.class);

    public final NumberPath<Long> shippingFee = createNumber("shippingFee", Long.class);

    public final NumberPath<Long> totalCouponDiscount = createNumber("totalCouponDiscount", Long.class);

    public QOrderProducts(String variable) {
        super(OrderProducts.class, forVariable(variable));
    }

    public QOrderProducts(Path<? extends OrderProducts> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderProducts(PathMetadata metadata) {
        super(OrderProducts.class, metadata);
    }

}

