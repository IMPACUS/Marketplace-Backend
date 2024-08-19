package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QProductForWebDTO is a Querydsl Projection type for ProductForWebDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductForWebDTO extends ConstructorExpression<ProductForWebDTO> {

    private static final long serialVersionUID = -440243386L;

    public QProductForWebDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> productNumber, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.DeliveryType> deliveryType, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductStatus> productStatus, com.querydsl.core.types.Expression<Long> stock, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<? extends java.util.Set<ProductOptionDTO>> productOptionList, com.querydsl.core.types.Expression<? extends java.util.Set<com.impacus.maketplace.dto.common.response.AttachFileDTO>> productImageList) {
        super(ProductForWebDTO.class, new Class<?>[]{long.class, String.class, int.class, String.class, com.impacus.maketplace.common.enumType.DeliveryType.class, com.impacus.maketplace.common.enumType.product.ProductStatus.class, long.class, java.time.LocalDateTime.class, java.util.Set.class, java.util.Set.class}, id, name, price, productNumber, deliveryType, productStatus, stock, createAt, productOptionList, productImageList);
    }

}

