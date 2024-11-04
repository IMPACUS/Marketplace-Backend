package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QAppProductDetailDTO is a Querydsl Projection type for AppProductDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAppProductDetailDTO extends ConstructorExpression<AppProductDetailDTO> {

    private static final long serialVersionUID = 1490016479L;

    public QAppProductDetailDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.entity.product.ProductOption>> options, com.querydsl.core.types.Expression<Long> wishlistId, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<? extends ProductDeliveryTimeDTO> deliveryTime, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<Long> sellerId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType, com.querydsl.core.types.Expression<Integer> sellerDeliveryFee) {
        super(AppProductDetailDTO.class, new Class<?>[]{long.class, String.class, int.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.util.List.class, long.class, int.class, String.class, String.class, ProductDeliveryTimeDTO.class, java.util.List.class, long.class, com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class, int.class}, id, name, appSalePrice, discountPrice, type, options, wishlistId, deliveryFee, brandName, description, deliveryTime, productImages, sellerId, deliveryFeeType, sellerDeliveryFee);
    }

}

