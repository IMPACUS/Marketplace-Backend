package com.impacus.maketplace.entity.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = 858619937L;

    public static final QSeller seller = new QSeller("seller");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.seller.BusinessType> businessType = createEnum("businessType", com.impacus.maketplace.common.enumType.seller.BusinessType.class);

    public final NumberPath<Integer> chargePercent = createNumber("chargePercent", Integer.class);

    public final StringPath contactName = createString("contactName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath customerServiceNumber = createString("customerServiceNumber");

    public final DateTimePath<java.time.LocalDateTime> entryApprovedAt = createDateTime("entryApprovedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.seller.EntryStatus> entryStatus = createEnum("entryStatus", com.impacus.maketplace.common.enumType.seller.EntryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> logoImageId = createNumber("logoImageId", Long.class);

    public final StringPath marketName = createString("marketName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final EnumPath<com.impacus.maketplace.common.enumType.seller.SellerType> sellerType = createEnum("sellerType", com.impacus.maketplace.common.enumType.seller.SellerType.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSeller(String variable) {
        super(Seller.class, forVariable(variable));
    }

    public QSeller(Path<? extends Seller> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeller(PathMetadata metadata) {
        super(Seller.class, metadata);
    }

}

