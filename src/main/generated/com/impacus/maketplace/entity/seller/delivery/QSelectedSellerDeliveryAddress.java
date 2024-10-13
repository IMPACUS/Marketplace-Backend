package com.impacus.maketplace.entity.seller.delivery;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSelectedSellerDeliveryAddress is a Querydsl query type for SelectedSellerDeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelectedSellerDeliveryAddress extends EntityPathBase<SelectedSellerDeliveryAddress> {

    private static final long serialVersionUID = -838832146L;

    public static final QSelectedSellerDeliveryAddress selectedSellerDeliveryAddress = new QSelectedSellerDeliveryAddress("selectedSellerDeliveryAddress");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerDeliveryAddressId = createNumber("sellerDeliveryAddressId", Long.class);

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QSelectedSellerDeliveryAddress(String variable) {
        super(SelectedSellerDeliveryAddress.class, forVariable(variable));
    }

    public QSelectedSellerDeliveryAddress(Path<? extends SelectedSellerDeliveryAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSelectedSellerDeliveryAddress(PathMetadata metadata) {
        super(SelectedSellerDeliveryAddress.class, metadata);
    }

}

