package com.impacus.maketplace.entity.temporaryProduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporaryProductDeliveryTime is a Querydsl query type for TemporaryProductDeliveryTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryProductDeliveryTime extends EntityPathBase<TemporaryProductDeliveryTime> {

    private static final long serialVersionUID = 1162200898L;

    public static final QTemporaryProductDeliveryTime temporaryProductDeliveryTime = new QTemporaryProductDeliveryTime("temporaryProductDeliveryTime");

    public final com.impacus.maketplace.entity.product.QDeliveryTime _super = new com.impacus.maketplace.entity.product.QDeliveryTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Integer> maxDays = _super.maxDays;

    //inherited
    public final NumberPath<Integer> minDays = _super.minDays;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> temporaryProductId = createNumber("temporaryProductId", Long.class);

    public QTemporaryProductDeliveryTime(String variable) {
        super(TemporaryProductDeliveryTime.class, forVariable(variable));
    }

    public QTemporaryProductDeliveryTime(Path<? extends TemporaryProductDeliveryTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporaryProductDeliveryTime(PathMetadata metadata) {
        super(TemporaryProductDeliveryTime.class, metadata);
    }

}

