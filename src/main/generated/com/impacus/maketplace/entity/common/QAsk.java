package com.impacus.maketplace.entity.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAsk is a Querydsl query type for Ask
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAsk extends EntityPathBase<Ask> {

    private static final long serialVersionUID = -442719869L;

    public static final QAsk ask = new QAsk("ask");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final EnumPath<com.impacus.maketplace.common.enumType.AskType> askType = createEnum("askType", com.impacus.maketplace.common.enumType.AskType.class);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAnswered = createBoolean("isAnswered");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> parentAskId = createNumber("parentAskId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath title = createString("title");

    public QAsk(String variable) {
        super(Ask.class, forVariable(variable));
    }

    public QAsk(Path<? extends Ask> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAsk(PathMetadata metadata) {
        super(Ask.class, metadata);
    }

}

