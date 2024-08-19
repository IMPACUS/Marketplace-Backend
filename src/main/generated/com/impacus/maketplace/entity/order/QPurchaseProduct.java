package com.impacus.maketplace.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseProduct is a Querydsl query type for PurchaseProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseProduct extends EntityPathBase<PurchaseProduct> {

    private static final long serialVersionUID = 2114934305L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseProduct purchaseProduct = new QPurchaseProduct("purchaseProduct");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath deliveryStatus = createString("deliveryStatus");

    public final StringPath exchangeStatus = createString("exchangeStatus");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final QOrder order;

    public final com.impacus.maketplace.entity.product.QProductDetailInfo productDetailInfo;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final StringPath returnStatus = createString("returnStatus");

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QPurchaseProduct(String variable) {
        this(PurchaseProduct.class, forVariable(variable), INITS);
    }

    public QPurchaseProduct(Path<? extends PurchaseProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseProduct(PathMetadata metadata, PathInits inits) {
        this(PurchaseProduct.class, metadata, inits);
    }

    public QPurchaseProduct(Class<? extends PurchaseProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
        this.productDetailInfo = inits.isInitialized("productDetailInfo") ? new com.impacus.maketplace.entity.product.QProductDetailInfo(forProperty("productDetailInfo")) : null;
    }

}

