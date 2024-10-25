package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductOption is a Querydsl query type for ProductOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOption extends EntityPathBase<ProductOption> {

    private static final long serialVersionUID = -2104319080L;

    public static final QProductOption productOption = new QProductOption("productOption");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath color = createString("color");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath size = createString("size");

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QProductOption(String variable) {
        super(ProductOption.class, forVariable(variable));
    }

    public QProductOption(Path<? extends ProductOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOption(PathMetadata metadata) {
        super(ProductOption.class, metadata);
    }

}

