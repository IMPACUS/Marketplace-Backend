package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QProductOptionForWebDTO is a Querydsl Projection type for ProductOptionForWebDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductOptionForWebDTO extends ConstructorExpression<ProductOptionForWebDTO> {

    private static final long serialVersionUID = 1734871249L;

    public QProductOptionForWebDTO(com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size) {
        super(ProductOptionForWebDTO.class, new Class<?>[]{long.class, String.class, String.class}, productOptionId, color, size);
    }

}

