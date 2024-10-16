package com.impacus.maketplace.repository.payment.checkout.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.payment.checkout.dto.QCheckoutProductWithDetailsDTO is a Querydsl Projection type for CheckoutProductWithDetailsDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCheckoutProductWithDetailsDTO extends ConstructorExpression<CheckoutProductWithDetailsDTO> {

    private static final long serialVersionUID = 1164302851L;

    public QCheckoutProductWithDetailsDTO(com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DiscountStatus> discountStatus, com.querydsl.core.types.Expression<Integer> appSalesPrice, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<Boolean> productIsDeleted, com.querydsl.core.types.Expression<Long> sellerId, com.querydsl.core.types.Expression<String> marketName, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size, com.querydsl.core.types.Expression<Long> stock, com.querydsl.core.types.Expression<Boolean> optionIsDeleted) {
        super(CheckoutProductWithDetailsDTO.class, new Class<?>[]{String.class, com.impacus.maketplace.common.enumType.product.ProductStatus.class, com.impacus.maketplace.common.enumType.product.ProductType.class, com.impacus.maketplace.common.enumType.DiscountStatus.class, int.class, int.class, int.class, java.util.List.class, boolean.class, long.class, String.class, String.class, String.class, long.class, boolean.class}, name, productStatus, type, discountStatus, appSalesPrice, discountPrice, deliveryFee, productImages, productIsDeleted, sellerId, marketName, color, size, stock, optionIsDeleted);
    }

}

