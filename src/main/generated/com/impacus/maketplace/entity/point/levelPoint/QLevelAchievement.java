package com.impacus.maketplace.entity.point.levelPoint;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLevelAchievement is a Querydsl query type for LevelAchievement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLevelAchievement extends EntityPathBase<LevelAchievement> {

    private static final long serialVersionUID = 1478579906L;

    public static final QLevelAchievement levelAchievement = new QLevelAchievement("levelAchievement");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final BooleanPath achievedBronze = createBoolean("achievedBronze");

    public final BooleanPath achievedGold = createBoolean("achievedGold");

    public final BooleanPath achievedRookie = createBoolean("achievedRookie");

    public final BooleanPath achievedSilver = createBoolean("achievedSilver");

    public final BooleanPath achievedVip = createBoolean("achievedVip");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final DateTimePath<java.time.LocalDateTime> recentAchievedBronzeAt = createDateTime("recentAchievedBronzeAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentAchievedGoldAt = createDateTime("recentAchievedGoldAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentAchievedRookieAt = createDateTime("recentAchievedRookieAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentAchievedSilverAt = createDateTime("recentAchievedSilverAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> recentAchievedVipAt = createDateTime("recentAchievedVipAt", java.time.LocalDateTime.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLevelAchievement(String variable) {
        super(LevelAchievement.class, forVariable(variable));
    }

    public QLevelAchievement(Path<? extends LevelAchievement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLevelAchievement(PathMetadata metadata) {
        super(LevelAchievement.class, metadata);
    }

}

