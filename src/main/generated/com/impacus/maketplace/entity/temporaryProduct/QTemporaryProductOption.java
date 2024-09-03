package com.impacus.maketplace.entity.temporaryProduct;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporaryProductOption is a Querydsl query type for TemporaryProductOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryProductOption extends EntityPathBase<TemporaryProductOption> {

    private static final long serialVersionUID = 1583162262L;

    public static final QTemporaryProductOption temporaryProductOption = new QTemporaryProductOption("temporaryProductOption");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final StringPath color = createString("color");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath size = createString("size");

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public final NumberPath<Long> temporaryProductId = createNumber("temporaryProductId", Long.class);

    public QTemporaryProductOption(String variable) {
        super(TemporaryProductOption.class, forVariable(variable));
    }

    public QTemporaryProductOption(Path<? extends TemporaryProductOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporaryProductOption(PathMetadata metadata) {
        super(TemporaryProductOption.class, metadata);
    }

}

