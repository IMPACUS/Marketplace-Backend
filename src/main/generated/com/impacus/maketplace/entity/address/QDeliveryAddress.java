package com.impacus.maketplace.entity.address;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryAddress is a Querydsl query type for DeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryAddress extends EntityPathBase<DeliveryAddress> {

    private static final long serialVersionUID = -322916679L;

    public static final QDeliveryAddress deliveryAddress = new QDeliveryAddress("deliveryAddress");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath connectNumber = createString("connectNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> paymentEventId = createNumber("paymentEventId", Long.class);

    public final StringPath postalCode = createString("postalCode");

    public final StringPath receiver = createString("receiver");

    //inherited
    public final StringPath registerId = _super.registerId;

    public QDeliveryAddress(String variable) {
        super(DeliveryAddress.class, forVariable(variable));
    }

    public QDeliveryAddress(Path<? extends DeliveryAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryAddress(PathMetadata metadata) {
        super(DeliveryAddress.class, metadata);
    }

}

