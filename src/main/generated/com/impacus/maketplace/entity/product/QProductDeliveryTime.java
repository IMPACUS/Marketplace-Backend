package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductDeliveryTime is a Querydsl query type for ProductDeliveryTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDeliveryTime extends EntityPathBase<ProductDeliveryTime> {

    private static final long serialVersionUID = 891796932L;

    public static final QProductDeliveryTime productDeliveryTime = new QProductDeliveryTime("productDeliveryTime");

    public final QDeliveryTime _super = new QDeliveryTime(this);

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

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public QProductDeliveryTime(String variable) {
        super(ProductDeliveryTime.class, forVariable(variable));
    }

    public QProductDeliveryTime(Path<? extends ProductDeliveryTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductDeliveryTime(PathMetadata metadata) {
        super(ProductDeliveryTime.class, metadata);
    }

}

