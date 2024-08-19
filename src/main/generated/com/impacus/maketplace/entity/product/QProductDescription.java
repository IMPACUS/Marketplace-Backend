package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductDescription is a Querydsl query type for ProductDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDescription extends EntityPathBase<ProductDescription> {

    private static final long serialVersionUID = 637527737L;

    public static final QProductDescription productDescription = new QProductDescription("productDescription");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QProductDescription(String variable) {
        super(ProductDescription.class, forVariable(variable));
    }

    public QProductDescription(Path<? extends ProductDescription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductDescription(PathMetadata metadata) {
        super(ProductDescription.class, metadata);
    }

}

