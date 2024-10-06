package com.impacus.maketplace.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSuperCategory is a Querydsl query type for SuperCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSuperCategory extends EntityPathBase<SuperCategory> {

    private static final long serialVersionUID = -1613506890L;

    public static final QSuperCategory superCategory = new QSuperCategory("superCategory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath registerId = _super.registerId;

    public QSuperCategory(String variable) {
        super(SuperCategory.class, forVariable(variable));
    }

    public QSuperCategory(Path<? extends SuperCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSuperCategory(PathMetadata metadata) {
        super(SuperCategory.class, metadata);
    }

}

