package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShoppingBasket is a Querydsl query type for ShoppingBasket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShoppingBasket extends EntityPathBase<ShoppingBasket> {

    private static final long serialVersionUID = 166265466L;

    public static final QShoppingBasket shoppingBasket = new QShoppingBasket("shoppingBasket");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final NumberPath<Long> productOptionId = createNumber("productOptionId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QShoppingBasket(String variable) {
        super(ShoppingBasket.class, forVariable(variable));
    }

    public QShoppingBasket(Path<? extends ShoppingBasket> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShoppingBasket(PathMetadata metadata) {
        super(ShoppingBasket.class, metadata);
    }

}

