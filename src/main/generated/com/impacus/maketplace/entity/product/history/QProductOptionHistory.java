package com.impacus.maketplace.entity.product.history;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductOptionHistory is a Querydsl query type for ProductOptionHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOptionHistory extends EntityPathBase<ProductOptionHistory> {

    private static final long serialVersionUID = -491852042L;

    public static final QProductOptionHistory productOptionHistory = new QProductOptionHistory("productOptionHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath color = createString("color");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> productOptionId = createNumber("productOptionId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath size = createString("size");

    public QProductOptionHistory(String variable) {
        super(ProductOptionHistory.class, forVariable(variable));
    }

    public QProductOptionHistory(Path<? extends ProductOptionHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOptionHistory(PathMetadata metadata) {
        super(ProductOptionHistory.class, metadata);
    }

}

