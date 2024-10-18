package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWishlist is a Querydsl query type for Wishlist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlist extends EntityPathBase<Wishlist> {

    private static final long serialVersionUID = -1747050511L;

    public static final QWishlist wishlist = new QWishlist("wishlist");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

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

    public QWishlist(String variable) {
        super(Wishlist.class, forVariable(variable));
    }

    public QWishlist(Path<? extends Wishlist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWishlist(PathMetadata metadata) {
        super(Wishlist.class, metadata);
    }

}

