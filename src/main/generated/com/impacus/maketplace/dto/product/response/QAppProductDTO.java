package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QAppProductDTO is a Querydsl Projection type for AppProductDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAppProductDTO extends ConstructorExpression<AppProductDTO> {

    private static final long serialVersionUID = -312346704L;

    public QAppProductDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<Long> wishlistId, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType, com.querydsl.core.types.Expression<Integer> sellerDeliveryFee) {
        super(AppProductDTO.class, new Class<?>[]{long.class, String.class, String.class, int.class, com.impacus.maketplace.common.enumType.product.DeliveryType.class, int.class, java.util.List.class, long.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.time.LocalDateTime.class, com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class, int.class}, productId, name, brandName, appSalePrice, deliveryType, discountPrice, productImages, wishlistId, deliveryFee, type, createAt, deliveryFeeType, sellerDeliveryFee);
    }

    public QAppProductDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType, com.querydsl.core.types.Expression<Integer> sellerDeliveryFee) {
        super(AppProductDTO.class, new Class<?>[]{long.class, String.class, String.class, int.class, com.impacus.maketplace.common.enumType.product.DeliveryType.class, int.class, java.util.List.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.time.LocalDateTime.class, com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class, int.class}, productId, name, brandName, appSalePrice, deliveryType, discountPrice, productImages, deliveryFee, type, createAt, deliveryFeeType, sellerDeliveryFee);
    }

}

