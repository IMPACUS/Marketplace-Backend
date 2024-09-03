package com.impacus.maketplace.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1327983939L;

    public static final QProduct product = new QProduct("product");

    public final com.impacus.maketplace.common.QBaseEntity _super = new com.impacus.maketplace.common.QBaseEntity(this);

    public final NumberPath<Integer> appSalesPrice = createNumber("appSalesPrice", Integer.class);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final EnumPath<com.impacus.maketplace.common.enumType.DeliveryCompany> deliveryCompany = createEnum("deliveryCompany", com.impacus.maketplace.common.enumType.DeliveryCompany.class);

    public final NumberPath<Integer> deliveryFee = createNumber("deliveryFee", Integer.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType = createEnum("deliveryFeeType", com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType = createEnum("deliveryType", com.impacus.maketplace.common.enumType.product.DeliveryType.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> discountPrice = createNumber("discountPrice", Integer.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.DiscountStatus> discountStatus = createEnum("discountStatus", com.impacus.maketplace.common.enumType.DiscountStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> marketPrice = createNumber("marketPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyAt = _super.modifyAt;

    //inherited
    public final StringPath modifyId = _super.modifyId;

    public final StringPath name = createString("name");

    public final ListPath<String, StringPath> productImages = this.<String, StringPath>createList("productImages", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath productNumber = createString("productNumber");

    public final EnumPath<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus = createEnum("productStatus", com.impacus.maketplace.common.enumType.product.ProductStatus.class);

    public final NumberPath<Integer> refundFee = createNumber("refundFee", Integer.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> refundFeeType = createEnum("refundFeeType", com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class);

    //inherited
    public final StringPath registerId = _super.registerId;

    public final NumberPath<Long> sellerId = createNumber("sellerId", Long.class);

    public final NumberPath<Integer> specialDeliveryFee = createNumber("specialDeliveryFee", Integer.class);

    public final NumberPath<Integer> specialRefundFee = createNumber("specialRefundFee", Integer.class);

    public final EnumPath<com.impacus.maketplace.common.enumType.product.ProductType> type = createEnum("type", com.impacus.maketplace.common.enumType.product.ProductType.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

