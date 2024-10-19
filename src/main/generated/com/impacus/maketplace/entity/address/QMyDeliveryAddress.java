package com.impacus.maketplace.entity.address;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyDeliveryAddress is a Querydsl query type for MyDeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyDeliveryAddress extends EntityPathBase<MyDeliveryAddress> {

    private static final long serialVersionUID = 1100077357L;

    public static final QMyDeliveryAddress myDeliveryAddress = new QMyDeliveryAddress("myDeliveryAddress");

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

    public final StringPath name = createString("name");

    public final StringPath postalCode = createString("postalCode");

    public final StringPath receiver = createString("receiver");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QMyDeliveryAddress(String variable) {
        super(MyDeliveryAddress.class, forVariable(variable));
    }

    public QMyDeliveryAddress(Path<? extends MyDeliveryAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyDeliveryAddress(PathMetadata metadata) {
        super(MyDeliveryAddress.class, metadata);
    }

}

