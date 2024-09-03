package com.impacus.maketplace.entity.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttachFileGroup is a Querydsl query type for AttachFileGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachFileGroup extends EntityPathBase<AttachFileGroup> {

    private static final long serialVersionUID = -126257752L;

    public static final QAttachFileGroup attachFileGroup = new QAttachFileGroup("attachFileGroup");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Long> attachFileId = createNumber("attachFileId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final EnumPath<com.impacus.maketplace.common.enumType.ReferencedEntityType> referencedEntity = createEnum("referencedEntity", com.impacus.maketplace.common.enumType.ReferencedEntityType.class);

    public final NumberPath<Long> referencedId = createNumber("referencedId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public QAttachFileGroup(String variable) {
        super(AttachFileGroup.class, forVariable(variable));
    }

    public QAttachFileGroup(Path<? extends AttachFileGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttachFileGroup(PathMetadata metadata) {
        super(AttachFileGroup.class, metadata);
    }

}

