package com.impacus.maketplace.entity.temporaryProduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporaryProductClaimInfo is a Querydsl query type for TemporaryProductClaimInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryProductClaimInfo extends EntityPathBase<TemporaryProductClaimInfo> {

    private static final long serialVersionUID = -1196128183L;

    public static final QTemporaryProductClaimInfo temporaryProductClaimInfo = new QTemporaryProductClaimInfo("temporaryProductClaimInfo");

    public final com.impacus.maketplace.entity.product.QClaimInfo _super = new com.impacus.maketplace.entity.product.QClaimInfo(this);

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

    //inherited
    public final StringPath recallInfo = _super.recallInfo;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> temporaryProductId = createNumber("temporaryProductId", Long.class);

    public QTemporaryProductClaimInfo(String variable) {
        super(TemporaryProductClaimInfo.class, forVariable(variable));
    }

    public QTemporaryProductClaimInfo(Path<? extends TemporaryProductClaimInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporaryProductClaimInfo(PathMetadata metadata) {
        super(TemporaryProductClaimInfo.class, metadata);
    }

}

