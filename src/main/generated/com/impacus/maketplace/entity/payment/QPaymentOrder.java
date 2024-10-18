package com.impacus.maketplace.entity.payment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentOrder is a Querydsl query type for PaymentOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOrder extends EntityPathBase<PaymentOrder> {

    private static final long serialVersionUID = -857012899L;

    public static final QPaymentOrder paymentOrder = new QPaymentOrder("paymentOrder");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Integer> commissionPercent = createNumber("commissionPercent", Integer.class);

    public final NumberPath<Long> couponDiscount = createNumber("couponDiscount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> ecoDiscount = createNumber("ecoDiscount", Long.class);

    public final NumberPath<Integer> failedCount = createNumber("failedCount", Integer.class);

    public final NumberPath<Long> greenLabelDiscount = createNumber("greenLabelDiscount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPaymentDone = createBoolean("isPaymentDone");

    public final BooleanPath ledgerUpdated = createBoolean("ledgerUpdated");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath orderId = createString("orderId");

    public final NumberPath<Long> paymentEventId = createNumber("paymentEventId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> productOptionHistoryId = createNumber("productOptionHistoryId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus> status = createEnum("status", com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus.class);

    public final NumberPath<Integer> threshold = createNumber("threshold", Integer.class);

    public final BooleanPath walletUpdated = createBoolean("walletUpdated");

    public QPaymentOrder(String variable) {
        super(PaymentOrder.class, forVariable(variable));
    }

    public QPaymentOrder(Path<? extends PaymentOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentOrder(PathMetadata metadata) {
        super(PaymentOrder.class, metadata);
    }

}

