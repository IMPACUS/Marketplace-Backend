package com.impacus.maketplace.entity.seller;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerBusinessInfo is a Querydsl query type for SellerBusinessInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerBusinessInfo extends EntityPathBase<SellerBusinessInfo> {

    private static final long serialVersionUID = -75603121L;

    public static final QSellerBusinessInfo sellerBusinessInfo = new QSellerBusinessInfo("sellerBusinessInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath businessAddress = createString("businessAddress");

    public final StringPath businessCondition = createString("businessCondition");

    public final StringPath businessEmail = createString("businessEmail");

    public final StringPath businessName = createString("businessName");

    public final StringPath businessRegistrationNumber = createString("businessRegistrationNumber");

    public final NumberPath<Long> copyBusinessRegistrationCertificateId = createNumber("copyBusinessRegistrationCertificateId", Long.class);

    public final NumberPath<Long> copyMainOrderBusinessReportCardId = createNumber("copyMainOrderBusinessReportCardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mailOrderBusinessReportNumber = createString("mailOrderBusinessReportNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath representativeContact = createString("representativeContact");

    public final StringPath representativeName = createString("representativeName");

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QSellerBusinessInfo(String variable) {
        super(SellerBusinessInfo.class, forVariable(variable));
    }

    public QSellerBusinessInfo(Path<? extends SellerBusinessInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerBusinessInfo(PathMetadata metadata) {
        super(SellerBusinessInfo.class, metadata);
    }

}

