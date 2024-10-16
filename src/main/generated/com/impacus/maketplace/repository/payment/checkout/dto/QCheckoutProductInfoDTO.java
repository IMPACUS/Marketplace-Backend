package com.impacus.maketplace.repository.payment.checkout.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.payment.checkout.dto.QCheckoutProductInfoDTO is a Querydsl Projection type for CheckoutProductInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCheckoutProductInfoDTO extends ConstructorExpression<CheckoutProductInfoDTO> {

    private static final long serialVersionUID = -55626255L;

    public QCheckoutProductInfoDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<Long> sellerId, com.querydsl.core.types.Expression<String> marketName, com.querydsl.core.types.Expression<Integer> chargePercent, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> productType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus, com.querydsl.core.types.Expression<Integer> appSalesPrice, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<Boolean> productIsDeleted, com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size, com.querydsl.core.types.Expression<Long> stock, com.querydsl.core.types.Expression<Boolean> optionIsDeleted, com.querydsl.core.types.Expression<Long> productOptionHistoryId) {
        super(CheckoutProductInfoDTO.class, new Class<?>[]{long.class, long.class, String.class, int.class, String.class, com.impacus.maketplace.common.enumType.product.ProductType.class, com.impacus.maketplace.common.enumType.product.ProductStatus.class, int.class, int.class, int.class, boolean.class, long.class, String.class, String.class, long.class, boolean.class, long.class}, productId, sellerId, marketName, chargePercent, name, productType, productStatus, appSalesPrice, discountPrice, deliveryFee, productIsDeleted, productOptionId, color, size, stock, optionIsDeleted, productOptionHistoryId);
    }

}

