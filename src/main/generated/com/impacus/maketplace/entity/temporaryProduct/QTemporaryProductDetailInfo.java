package com.impacus.maketplace.entity.temporaryProduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporaryProductDetailInfo is a Querydsl query type for TemporaryProductDetailInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryProductDetailInfo extends EntityPathBase<TemporaryProductDetailInfo> {

    private static final long serialVersionUID = 1305772864L;

    public static final QTemporaryProductDetailInfo temporaryProductDetailInfo = new QTemporaryProductDetailInfo("temporaryProductDetailInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath asManager = createString("asManager");

    public final StringPath contactNumber = createString("contactNumber");

    public final StringPath countryOfManufacture = createString("countryOfManufacture");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath dateOfManufacture = createString("dateOfManufacture");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath importer = createString("importer");

    public final StringPath manufacturer = createString("manufacturer");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath productColor = createString("productColor");

    public final StringPath productMaterial = createString("productMaterial");

    public final StringPath productSize = createString("productSize");

    public final StringPath productType = createString("productType");

    public final StringPath qualityAssuranceStandards = createString("qualityAssuranceStandards");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> temporaryProductId = createNumber("temporaryProductId", Long.class);

    public final StringPath washingPrecautions = createString("washingPrecautions");

    public QTemporaryProductDetailInfo(String variable) {
        super(TemporaryProductDetailInfo.class, forVariable(variable));
    }

    public QTemporaryProductDetailInfo(Path<? extends TemporaryProductDetailInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporaryProductDetailInfo(PathMetadata metadata) {
        super(TemporaryProductDetailInfo.class, metadata);
    }

}

