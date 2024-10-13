package com.impacus.maketplace.entity.point.levelPoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLevelPointMaster is a Querydsl query type for LevelPointMaster
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLevelPointMaster extends EntityPathBase<LevelPointMaster> {

    private static final long serialVersionUID = -202061915L;

    public static final QLevelPointMaster levelPointMaster = new QLevelPointMaster("levelPointMaster");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> expirationStartAt = createDateTime("expirationStartAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> levelPoint = createNumber("levelPoint", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.user.UserLevel> userLevel = createEnum("userLevel", com.impacus.maketplace.common.enumType.user.UserLevel.class);

    public QLevelPointMaster(String variable) {
        super(LevelPointMaster.class, forVariable(variable));
    }

    public QLevelPointMaster(Path<? extends LevelPointMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLevelPointMaster(PathMetadata metadata) {
        super(LevelPointMaster.class, metadata);
    }

}

