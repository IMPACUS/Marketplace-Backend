package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductClaimInfo is a Querydsl query type for ProductClaimInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductClaimInfo extends EntityPathBase<ProductClaimInfo> {

    private static final long serialVersionUID = 1720709383L;

    public static final QProductClaimInfo productClaimInfo = new QProductClaimInfo("productClaimInfo");

    public final QClaimInfo _super = new QClaimInfo(this);

    //inherited
    public final StringPath claimContactInfo = _super.claimContactInfo;

    //inherited
    public final StringPath claimCost = _super.claimCost;

    //inherited
    public final StringPath claimPolicyGuild = _super.claimPolicyGuild;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    //inherited
    public final StringPath recallInfo = _super.recallInfo;

    //inherited
    public final StringPath registerId = _super.registerId;

    public QProductClaimInfo(String variable) {
        super(ProductClaimInfo.class, forVariable(variable));
    }

    public QProductClaimInfo(Path<? extends ProductClaimInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductClaimInfo(PathMetadata metadata) {
        super(ProductClaimInfo.class, metadata);
    }

}

