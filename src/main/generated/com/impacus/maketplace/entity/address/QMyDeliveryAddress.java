package com.impacus.maketplace.entity.address;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyDeliveryAddress is a Querydsl query type for MyDeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyDeliveryAddress extends EntityPathBase<MyDeliveryAddress> {

    private static final long serialVersionUID = 1100077357L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyDeliveryAddress myDeliveryAddress = new QMyDeliveryAddress("myDeliveryAddress");

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

    public final com.impacus.maketplace.entity.user.QUser user;

    public QMyDeliveryAddress(String variable) {
        this(MyDeliveryAddress.class, forVariable(variable), INITS);
    }

    public QMyDeliveryAddress(Path<? extends MyDeliveryAddress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyDeliveryAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyDeliveryAddress(PathMetadata metadata, PathInits inits) {
        this(MyDeliveryAddress.class, metadata, inits);
    }

    public QMyDeliveryAddress(Class<? extends MyDeliveryAddress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.impacus.maketplace.entity.user.QUser(forProperty("user")) : null;
    }

}

