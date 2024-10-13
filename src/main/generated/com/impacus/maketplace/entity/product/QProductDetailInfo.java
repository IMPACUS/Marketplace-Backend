package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductDetailInfo is a Querydsl query type for ProductDetailInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDetailInfo extends EntityPathBase<ProductDetailInfo> {

    private static final long serialVersionUID = 1533424194L;

    public static final QProductDetailInfo productDetailInfo = new QProductDetailInfo("productDetailInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath asManager = createString("asManager");

    public final StringPath contactNumber = createString("contactNumber");

    public final StringPath countryOfManufacture = createString("countryOfManufacture");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath dateOfManufacture = createString("dateOfManufacture");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath importer = createString("importer");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath manufacturer = createString("manufacturer");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath productColor = createString("productColor");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productMaterial = createString("productMaterial");

    public final StringPath productSize = createString("productSize");

    public final StringPath productType = createString("productType");

    public final StringPath qualityAssuranceStandards = createString("qualityAssuranceStandards");

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath washingPrecautions = createString("washingPrecautions");

    public QProductDetailInfo(String variable) {
        super(ProductDetailInfo.class, forVariable(variable));
    }

    public QProductDetailInfo(Path<? extends ProductDetailInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductDetailInfo(PathMetadata metadata) {
        super(ProductDetailInfo.class, metadata);
    }

}

