package com.impacus.maketplace.dto.shoppingBasket.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.shoppingBasket.response.QProductShoppingBasketDTO is a Querydsl Projection type for ProductShoppingBasketDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductShoppingBasketDTO extends ConstructorExpression<ProductShoppingBasketDTO> {

    private static final long serialVersionUID = 1852134204L;

    public QProductShoppingBasketDTO(com.querydsl.core.types.Expression<Long> shoppingBasketId, com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<? extends com.impacus.maketplace.dto.product.response.ProductOptionDTO> productOption, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<Long> groupId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType, com.querydsl.core.types.Expression<Integer> sellerDeliveryFee, com.querydsl.core.types.Expression<Long> quantity, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifyAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryFeeRule> deliveryFeeRule) {
        super(ProductShoppingBasketDTO.class, new Class<?>[]{long.class, long.class, String.class, java.util.List.class, String.class, int.class, com.impacus.maketplace.dto.product.response.ProductOptionDTO.class, com.impacus.maketplace.common.enumType.product.DeliveryType.class, com.impacus.maketplace.common.enumType.product.ProductType.class, int.class, long.class, com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class, int.class, long.class, java.time.LocalDateTime.class, com.impacus.maketplace.common.enumType.product.DeliveryFeeRule.class}, shoppingBasketId, productId, name, productImages, brandName, deliveryFee, productOption, deliveryType, type, discountPrice, groupId, deliveryFeeType, sellerDeliveryFee, quantity, modifyAt, deliveryFeeRule);
    }

}

