package com.impacus.maketplace.entity.payment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentOrderHistory is a Querydsl query type for PaymentOrderHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOrderHistory extends EntityPathBase<PaymentOrderHistory> {

    private static final long serialVersionUID = -1579473801L;

    public static final QPaymentOrderHistory paymentOrderHistory = new QPaymentOrderHistory("paymentOrderHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final EnumPath<com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus> newStatus = createEnum("newStatus", com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus.class);

    public final NumberPath<Long> paymentOrderId = createNumber("paymentOrderId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus> previousStatus = createEnum("previousStatus", com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus.class);

    public final StringPath reason = createString("reason");

    //inherited
    public final StringPath registerId = _super.registerId;

    public QPaymentOrderHistory(String variable) {
        super(PaymentOrderHistory.class, forVariable(variable));
    }

    public QPaymentOrderHistory(Path<? extends PaymentOrderHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentOrderHistory(PathMetadata metadata) {
        super(PaymentOrderHistory.class, metadata);
    }

}

