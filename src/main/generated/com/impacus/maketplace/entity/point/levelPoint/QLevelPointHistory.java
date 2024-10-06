package com.impacus.maketplace.entity.point.levelPoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLevelPointHistory is a Querydsl query type for LevelPointHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLevelPointHistory extends EntityPathBase<LevelPointHistory> {

    private static final long serialVersionUID = -1882460239L;

    public static final QLevelPointHistory levelPointHistory = new QLevelPointHistory("levelPointHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final BooleanPath hasReceivedLevelUpPoints = createBoolean("hasReceivedLevelUpPoints");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointStatus> pointStatus = createEnum("pointStatus", com.impacus.maketplace.common.enumType.point.PointStatus.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.point.PointType> pointType = createEnum("pointType", com.impacus.maketplace.common.enumType.point.PointType.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> tradeAmount = createNumber("tradeAmount", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLevelPointHistory(String variable) {
        super(LevelPointHistory.class, forVariable(variable));
    }

    public QLevelPointHistory(Path<? extends LevelPointHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLevelPointHistory(PathMetadata metadata) {
        super(LevelPointHistory.class, metadata);
    }

}

