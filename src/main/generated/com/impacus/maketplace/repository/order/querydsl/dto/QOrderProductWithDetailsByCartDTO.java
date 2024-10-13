package com.impacus.maketplace.repository.order.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.order.querydsl.dto.QOrderProductWithDetailsByCartDTO is a Querydsl Projection type for OrderProductWithDetailsByCartDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QOrderProductWithDetailsByCartDTO extends ConstructorExpression<OrderProductWithDetailsByCartDTO> {

    private static final long serialVersionUID = 2034092111L;

    public QOrderProductWithDetailsByCartDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DiscountStatus> discountStatus, com.querydsl.core.types.Expression<Integer> appSalesPrice, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<Boolean> productIsDeleted, com.querydsl.core.types.Expression<String> marketName, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size, com.querydsl.core.types.Expression<Long> stock, com.querydsl.core.types.Expression<Boolean> optionIsDeleted, com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<Long> quantity) {
        super(OrderProductWithDetailsByCartDTO.class, new Class<?>[]{long.class, String.class, com.impacus.maketplace.common.enumType.product.ProductStatus.class, com.impacus.maketplace.common.enumType.product.ProductType.class, com.impacus.maketplace.common.enumType.DiscountStatus.class, int.class, int.class, int.class, java.util.List.class, boolean.class, String.class, String.class, String.class, long.class, boolean.class, long.class, long.class}, productId, name, productStatus, type, discountStatus, appSalesPrice, discountPrice, deliveryFee, productImages, productIsDeleted, marketName, color, size, stock, optionIsDeleted, productOptionId, quantity);
    }

}

