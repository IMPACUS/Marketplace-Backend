package com.impacus.maketplace.dto.shoppingBasket.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.shoppingBasket.response.QShoppingBasketDetailDTO is a Querydsl Projection type for ShoppingBasketDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QShoppingBasketDetailDTO extends ConstructorExpression<ShoppingBasketDetailDTO> {

    private static final long serialVersionUID = 537429030L;

    public QShoppingBasketDetailDTO(com.querydsl.core.types.Expression<Long> shoppingBasketId, com.querydsl.core.types.Expression<Long> quantity, com.querydsl.core.types.Expression<? extends com.impacus.maketplace.dto.product.response.ProductForAppDTO> product, com.querydsl.core.types.Expression<? extends com.impacus.maketplace.dto.product.response.ProductOptionDTO> productOption) {
        super(ShoppingBasketDetailDTO.class, new Class<?>[]{long.class, long.class, com.impacus.maketplace.dto.product.response.ProductForAppDTO.class, com.impacus.maketplace.dto.product.response.ProductOptionDTO.class}, shoppingBasketId, quantity, product, productOption);
    }

}

