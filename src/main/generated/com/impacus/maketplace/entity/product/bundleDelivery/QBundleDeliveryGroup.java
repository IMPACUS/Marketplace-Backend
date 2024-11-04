package com.impacus.maketplace.entity.product.bundleDelivery;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBundleDeliveryGroup is a Querydsl query type for BundleDeliveryGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBundleDeliveryGroup extends EntityPathBase<BundleDeliveryGroup> {

    private static final long serialVersionUID = 580135197L;

    public static final QBundleDeliveryGroup bundleDeliveryGroup = new QBundleDeliveryGroup("bundleDeliveryGroup");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final EnumPath<com.impacus.maketplace.common.enumType.product.DeliveryFeeRule> deliveryFeeRule = createEnum("deliveryFeeRule", com.impacus.maketplace.common.enumType.product.DeliveryFeeRule.class);

    public final StringPath groupNumber = createString("groupNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isUsed = createBoolean("isUsed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QBundleDeliveryGroup(String variable) {
        super(BundleDeliveryGroup.class, forVariable(variable));
    }

    public QBundleDeliveryGroup(Path<? extends BundleDeliveryGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBundleDeliveryGroup(PathMetadata metadata) {
        super(BundleDeliveryGroup.class, metadata);
    }

}

