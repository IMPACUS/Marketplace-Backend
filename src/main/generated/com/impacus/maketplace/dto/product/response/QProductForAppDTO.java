package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QProductForAppDTO is a Querydsl Projection type for ProductForAppDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductForAppDTO extends ConstructorExpression<ProductForAppDTO> {

    private static final long serialVersionUID = -1059508903L;

    public QProductForAppDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DeliveryType> deliveryType, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.dto.common.response.AttachFileDTO>> productImageList, com.querydsl.core.types.Expression<Long> wishlistId, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(ProductForAppDTO.class, new Class<?>[]{long.class, String.class, String.class, int.class, com.impacus.maketplace.common.enumType.DeliveryType.class, int.class, java.util.List.class, long.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.time.LocalDateTime.class}, productId, name, brandName, appSalePrice, deliveryType, discountPrice, productImageList, wishlistId, deliveryFee, type, createAt);
    }

    public QProductForAppDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DeliveryType> deliveryType, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.dto.common.response.AttachFileDTO>> productImageList, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(ProductForAppDTO.class, new Class<?>[]{long.class, String.class, String.class, int.class, com.impacus.maketplace.common.enumType.DeliveryType.class, int.class, java.util.List.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.time.LocalDateTime.class}, productId, name, brandName, appSalePrice, deliveryType, discountPrice, productImageList, deliveryFee, type, createAt);
    }

}

