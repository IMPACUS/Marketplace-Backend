package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QDetailedProductDTO is a Querydsl Projection type for DetailedProductDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QDetailedProductDTO extends ConstructorExpression<DetailedProductDTO> {

    private static final long serialVersionUID = 1790939745L;

    public QDetailedProductDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<Integer> appSalePrice, com.querydsl.core.types.Expression<Integer> discountPrice, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> type, com.querydsl.core.types.Expression<? extends java.util.List<com.impacus.maketplace.entity.product.ProductOption>> options, com.querydsl.core.types.Expression<Long> wishlistId, com.querydsl.core.types.Expression<Integer> deliveryFee, com.querydsl.core.types.Expression<String> brandName, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<? extends ProductDeliveryTimeDTO> deliveryTime) {
        super(DetailedProductDTO.class, new Class<?>[]{long.class, String.class, int.class, int.class, com.impacus.maketplace.common.enumType.product.ProductType.class, java.util.List.class, long.class, int.class, String.class, String.class, ProductDeliveryTimeDTO.class}, id, name, appSalePrice, discountPrice, type, options, wishlistId, deliveryFee, brandName, description, deliveryTime);
    }

}

