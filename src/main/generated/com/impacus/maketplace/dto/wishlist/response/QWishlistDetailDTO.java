package com.impacus.maketplace.dto.wishlist.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.wishlist.response.QWishlistDetailDTO is a Querydsl Projection type for WishlistDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWishlistDetailDTO extends ConstructorExpression<WishlistDetailDTO> {

    private static final long serialVersionUID = 1156119928L;

    public QWishlistDetailDTO(com.querydsl.core.types.Expression<Long> wishlistId, com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DeliveryType> deliveryType, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.dto.common.response.AttachFileDTO>> productImageList) {
        super(WishlistDetailDTO.class, new Class<?>[]{long.class, long.class, String.class, String.class, int.class, com.impacus.maketplace.common.enumType.DeliveryType.class, int.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.time.LocalDateTime.class, java.util.List.class}, wishlistId, productId, name, brandName, appSalePrice, deliveryType, discountPrice, deliveryFee, type, createAt, productImageList);
    }

}

