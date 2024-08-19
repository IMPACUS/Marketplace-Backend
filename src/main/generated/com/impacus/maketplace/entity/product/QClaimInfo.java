package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClaimInfo is a Querydsl query type for ClaimInfo
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QClaimInfo extends EntityPathBase<ClaimInfo> {

    private static final long serialVersionUID = 1337253470L;

    public static final QClaimInfo claimInfo = new QClaimInfo("claimInfo");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath claimContactInfo = createString("claimContactInfo");

    public final StringPath claimCost = createString("claimCost");

    public final StringPath claimPolicyGuild = createString("claimPolicyGuild");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath recallInfo = createString("recallInfo");

    //inherited
    public final StringPath registerId = _super.registerId;

    public QClaimInfo(String variable) {
        super(ClaimInfo.class, forVariable(variable));
    }

    public QClaimInfo(Path<? extends ClaimInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClaimInfo(PathMetadata metadata) {
        super(ClaimInfo.class, metadata);
    }

}

