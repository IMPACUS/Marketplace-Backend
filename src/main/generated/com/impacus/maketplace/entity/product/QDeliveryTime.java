package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryTime is a Querydsl query type for DeliveryTime
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QDeliveryTime extends EntityPathBase<DeliveryTime> {

    private static final long serialVersionUID = 1969700109L;

    public static final QDeliveryTime deliveryTime = new QDeliveryTime("deliveryTime");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Integer> maxDays = createNumber("maxDays", Integer.class);

    public final NumberPath<Integer> minDays = createNumber("minDays", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public QDeliveryTime(String variable) {
        super(DeliveryTime.class, forVariable(variable));
    }

    public QDeliveryTime(Path<? extends DeliveryTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryTime(PathMetadata metadata) {
        super(DeliveryTime.class, metadata);
    }

}

