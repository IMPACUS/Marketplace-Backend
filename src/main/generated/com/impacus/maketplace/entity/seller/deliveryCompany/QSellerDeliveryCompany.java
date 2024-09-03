package com.impacus.maketplace.entity.seller.deliveryCompany;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerDeliveryCompany is a Querydsl query type for SellerDeliveryCompany
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerDeliveryCompany extends EntityPathBase<SellerDeliveryCompany> {

    private static final long serialVersionUID = -70576829L;

    public static final QSellerDeliveryCompany sellerDeliveryCompany = new QSellerDeliveryCompany("sellerDeliveryCompany");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Integer> generalDeliveryFee = createNumber("generalDeliveryFee", Integer.class);

    public final NumberPath<Integer> generalSpecialDeliveryFee = createNumber("generalSpecialDeliveryFee", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Integer> refundDeliveryFee = createNumber("refundDeliveryFee", Integer.class);

    public final NumberPath<Integer> refundSpecialDeliveryFee = createNumber("refundSpecialDeliveryFee", Integer.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QSellerDeliveryCompany(String variable) {
        super(SellerDeliveryCompany.class, forVariable(variable));
    }

    public QSellerDeliveryCompany(Path<? extends SellerDeliveryCompany> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerDeliveryCompany(PathMetadata metadata) {
        super(SellerDeliveryCompany.class, metadata);
    }

}

