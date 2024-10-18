package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QWebProductTableDetailDTO is a Querydsl Projection type for WebProductTableDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebProductTableDetailDTO extends ConstructorExpression<WebProductTableDetailDTO> {

    private static final long serialVersionUID = 1767832028L;

    public QWebProductTableDetailDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> price, com.querydsl.core.types.Expression<String> productNumber, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.DeliveryType> deliveryType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.entity.product.ProductOption>> productOptions, com.querydsl.core.types.Expression<? extends java.util.List<String>> productImages) {
        super(WebProductTableDetailDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, com.impacus.maketplace.common.enumType.product.DeliveryType.class, com.impacus.maketplace.common.enumType.product.ProductStatus.class, java.time.LocalDateTime.class, java.util.List.class, java.util.List.class}, id, name, price, productNumber, deliveryType, productStatus, createAt, productOptions, productImages);
    }

}

