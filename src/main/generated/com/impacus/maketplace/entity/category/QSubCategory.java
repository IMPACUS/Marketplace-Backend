package com.impacus.maketplace.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubCategory is a Querydsl query type for SubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubCategory extends EntityPathBase<SubCategory> {

    private static final long serialVersionUID = 312030139L;

    public static final QSubCategory subCategory = new QSubCategory("subCategory");

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

    public final NumberPath<Long> superCategoryId = createNumber("superCategoryId", Long.class);

    public final NumberPath<Long> thumbnailId = createNumber("thumbnailId", Long.class);

    public QSubCategory(String variable) {
        super(SubCategory.class, forVariable(variable));
    }

    public QSubCategory(Path<? extends SubCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubCategory(PathMetadata metadata) {
        super(SubCategory.class, metadata);
    }

}

