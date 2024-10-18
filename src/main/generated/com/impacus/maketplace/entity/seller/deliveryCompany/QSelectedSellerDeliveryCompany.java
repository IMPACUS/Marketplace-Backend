package com.impacus.maketplace.entity.seller.deliveryCompany;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSelectedSellerDeliveryCompany is a Querydsl query type for SelectedSellerDeliveryCompany
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelectedSellerDeliveryCompany extends EntityPathBase<SelectedSellerDeliveryCompany> {

    private static final long serialVersionUID = 1024444520L;

    public static final QSelectedSellerDeliveryCompany selectedSellerDeliveryCompany = new QSelectedSellerDeliveryCompany("selectedSellerDeliveryCompany");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final EnumPath<com.impacus.maketplace.common.enumType.DeliveryCompany> deliveryCompany = createEnum("deliveryCompany", com.impacus.maketplace.common.enumType.DeliveryCompany.class);

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerDeliveryCompanyId = createNumber("sellerDeliveryCompanyId", Long.class);

    public QSelectedSellerDeliveryCompany(String variable) {
        super(SelectedSellerDeliveryCompany.class, forVariable(variable));
    }

    public QSelectedSellerDeliveryCompany(Path<? extends SelectedSellerDeliveryCompany> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSelectedSellerDeliveryCompany(PathMetadata metadata) {
        super(SelectedSellerDeliveryCompany.class, metadata);
    }

}

