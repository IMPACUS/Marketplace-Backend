package com.impacus.maketplace.entity.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerAdjustmentInfo is a Querydsl query type for SellerAdjustmentInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerAdjustmentInfo extends EntityPathBase<SellerAdjustmentInfo> {

    private static final long serialVersionUID = 1935230524L;

    public static final QSellerAdjustmentInfo sellerAdjustmentInfo = new QSellerAdjustmentInfo("sellerAdjustmentInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath accountName = createString("accountName");

    public final StringPath accountNumber = createString("accountNumber");

    public final EnumPath<com.impacus.maketplace.common.enumType.BankCode> bankCode = createEnum("bankCode", com.impacus.maketplace.common.enumType.BankCode.class);

    public final NumberPath<Long> copyBankBookId = createNumber("copyBankBookId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QSellerAdjustmentInfo(String variable) {
        super(SellerAdjustmentInfo.class, forVariable(variable));
    }

    public QSellerAdjustmentInfo(Path<? extends SellerAdjustmentInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerAdjustmentInfo(PathMetadata metadata) {
        super(SellerAdjustmentInfo.class, metadata);
    }

}

