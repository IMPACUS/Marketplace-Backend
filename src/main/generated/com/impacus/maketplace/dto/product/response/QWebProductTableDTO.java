package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QWebProductTableDTO is a Querydsl Projection type for WebProductTableDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebProductTableDTO extends ConstructorExpression<WebProductTableDTO> {

    private static final long serialVersionUID = 1546181229L;

    public QWebProductTableDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages, com.querydsl.core.types.Expression<String> productNumber, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.entity.product.ProductOption>> productOptions, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryRefundType> deliveryFeeType, com.querydsl.core.types.Expression<Integer> sellerDeliveryFee) {
        super(WebProductTableDTO.class, new Class<?>[]{long.class, String.class, java.util.List.class, String.class, com.impacus.maketplace.common.enumType.product.DeliveryType.class, java.time.LocalDateTime.class, java.util.List.class, int.class, com.impacus.maketplace.common.enumType.product.DeliveryRefundType.class, int.class}, id, name, productImages, productNumber, deliveryType, createAt, productOptions, deliveryFee, deliveryFeeType, sellerDeliveryFee);
    }

}

