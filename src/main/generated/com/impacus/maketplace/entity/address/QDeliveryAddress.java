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

    public final QAddress _super = new QAddress(this);

    //inherited
    public final StringPath address = _super.address;

    //inherited
    public final StringPath connectNumber = _super.connectNumber;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final StringPath detailAddress = _super.detailAddress;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath memo = _super.memo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath postalCode = _super.postalCode;

    //inherited
    public final StringPath receiver = _super.receiver;

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

