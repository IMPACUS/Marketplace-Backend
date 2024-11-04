package com.impacus.maketplace.entity.product.history;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductHistory is a Querydsl query type for ProductHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductHistory extends EntityPathBase<ProductHistory> {

    private static final long serialVersionUID = 105929323L;

    public static final QProductHistory productHistory = new QProductHistory("productHistory");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final ListPath<String, StringPath> productImages = this.<String, StringPath>createList("productImages", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final StringPath registerId = _super.registerId;

    public QProductHistory(String variable) {
        super(ProductHistory.class, forVariable(variable));
    }

    public QProductHistory(Path<? extends ProductHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductHistory(PathMetadata metadata) {
        super(ProductHistory.class, metadata);
    }

}

