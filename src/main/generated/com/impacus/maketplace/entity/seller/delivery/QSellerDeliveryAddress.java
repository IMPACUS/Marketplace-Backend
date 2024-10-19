package com.impacus.maketplace.entity.seller.delivery;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerDeliveryAddress is a Querydsl query type for SellerDeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerDeliveryAddress extends EntityPathBase<SellerDeliveryAddress> {

    private static final long serialVersionUID = -1636811831L;

    public static final QSellerDeliveryAddress sellerDeliveryAddress = new QSellerDeliveryAddress("sellerDeliveryAddress");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath generalAddress = createString("generalAddress");

    public final StringPath generalBusinessName = createString("generalBusinessName");

    public final StringPath generalDetailAddress = createString("generalDetailAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath refundAccountName = createString("refundAccountName");

    public final StringPath refundAccountNumber = createString("refundAccountNumber");

    public final StringPath refundAddress = createString("refundAddress");

    public final EnumPath<com.impacus.maketplace.common.enumType.BankCode> refundBankCode = createEnum("refundBankCode", com.impacus.maketplace.common.enumType.BankCode.class);

    public final StringPath refundBusinessName = createString("refundBusinessName");

    public final StringPath refundDetailAddress = createString("refundDetailAddress");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QSellerDeliveryAddress(String variable) {
        super(SellerDeliveryAddress.class, forVariable(variable));
    }

    public QSellerDeliveryAddress(Path<? extends SellerDeliveryAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerDeliveryAddress(PathMetadata metadata) {
        super(SellerDeliveryAddress.class, metadata);
    }

}

