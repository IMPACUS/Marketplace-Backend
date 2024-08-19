package com.impacus.maketplace.entity.temporaryProduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporaryProductDescription is a Querydsl query type for TemporaryProductDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryProductDescription extends EntityPathBase<TemporaryProductDescription> {

    private static final long serialVersionUID = -2124696197L;

    public static final QTemporaryProductDescription temporaryProductDescription = new QTemporaryProductDescription("temporaryProductDescription");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> temporaryProductId = createNumber("temporaryProductId", Long.class);

    public QTemporaryProductDescription(String variable) {
        super(TemporaryProductDescription.class, forVariable(variable));
    }

    public QTemporaryProductDescription(Path<? extends TemporaryProductDescription> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporaryProductDescription(PathMetadata metadata) {
        super(TemporaryProductDescription.class, metadata);
    }

}

