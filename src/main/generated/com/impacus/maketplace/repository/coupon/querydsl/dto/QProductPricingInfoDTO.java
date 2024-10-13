package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.coupon.querydsl.dto.QProductPricingInfoDTO is a Querydsl Projection type for ProductPricingInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductPricingInfoDTO extends ConstructorExpression<ProductPricingInfoDTO> {

    private static final long serialVersionUID = 135407092L;

    public QProductPricingInfoDTO(com.querydsl.core.types.Expression<Long> productId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.product.ProductType> productType, com.querydsl.core.types.Expression<Integer> appSalesPrice, com.querydsl.core.types.Expression<String> marketName) {
        super(ProductPricingInfoDTO.class, new Class<?>[]{long.class, com.impacus.maketplace.common.enumType.product.ProductType.class, int.class, String.class}, productId, productType, appSalesPrice, marketName);
    }

}

