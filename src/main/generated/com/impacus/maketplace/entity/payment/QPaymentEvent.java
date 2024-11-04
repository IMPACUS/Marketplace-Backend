package com.impacus.maketplace.entity.payment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentEvent is a Querydsl query type for PaymentEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEvent extends EntityPathBase<PaymentEvent> {

    private static final long serialVersionUID = -866127703L;

    public static final QPaymentEvent paymentEvent = new QPaymentEvent("paymentEvent");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> approvedAt = createDateTime("approvedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> buyerId = createNumber("buyerId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPaymentDone = createBoolean("isPaymentDone");

    public final EnumPath<com.impacus.maketplace.common.enumType.payment.PaymentMethod> method = createEnum("method", com.impacus.maketplace.common.enumType.payment.PaymentMethod.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath orderId = createString("orderId");

    public final StringPath orderName = createString("orderName");

    public final StringPath paymentKey = createString("paymentKey");

    public final MapPath<String, Object, SimplePath<Object>> pspRawData = this.<String, Object, SimplePath<Object>>createMap("pspRawData", String.class, Object.class, SimplePath.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.payment.PaymentType> type = createEnum("type", com.impacus.maketplace.common.enumType.payment.PaymentType.class);

    public QPaymentEvent(String variable) {
        super(PaymentEvent.class, forVariable(variable));
    }

    public QPaymentEvent(Path<? extends PaymentEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentEvent(PathMetadata metadata) {
        super(PaymentEvent.class, metadata);
    }

}

