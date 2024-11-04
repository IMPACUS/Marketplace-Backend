package com.impacus.maketplace.entity.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 1047702017L;

    public static final QReview review = new QReview("review");

    public final DateTimePath<java.time.ZonedDateTime> archiveAt = createDateTime("archiveAt", java.time.ZonedDateTime.class);

    public final StringPath buyerContents = createString("buyerContents");

    public final NumberPath<Long> buyerId = createNumber("buyerId", Long.class);

    public final NumberPath<Long> buyerUploadImgId = createNumber("buyerUploadImgId", Long.class);

    public final DateTimePath<java.time.ZonedDateTime> createAt = createDateTime("createAt", java.time.ZonedDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isArchive = createBoolean("isArchive");

    public final BooleanPath isComment = createBoolean("isComment");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final StringPath sellerComment = createString("sellerComment");

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public QReview(String variable) {
        super(Review.class, forVariable(variable));
    }

    public QReview(Path<? extends Review> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReview(PathMetadata metadata) {
        super(Review.class, metadata);
    }

}

