package com.impacus.maketplace.entity.sharedLink;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSharedLink is a Querydsl query type for SharedLink
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSharedLink extends EntityPathBase<SharedLink> {

    private static final long serialVersionUID = 298925473L;

    public static final QSharedLink sharedLink = new QSharedLink("sharedLink");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sharedUserId = createNumber("sharedUserId", Long.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.SharedLinkType> type = createEnum("type", com.impacus.maketplace.common.enumType.SharedLinkType.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSharedLink(String variable) {
        super(SharedLink.class, forVariable(variable));
    }

    public QSharedLink(Path<? extends SharedLink> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSharedLink(PathMetadata metadata) {
        super(SharedLink.class, metadata);
    }

}

