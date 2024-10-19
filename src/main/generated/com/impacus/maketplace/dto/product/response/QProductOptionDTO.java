package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QProductOptionDTO is a Querydsl Projection type for ProductOptionDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductOptionDTO extends ConstructorExpression<ProductOptionDTO> {

    private static final long serialVersionUID = 220303452L;

    public QProductOptionDTO(com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size) {
        super(ProductOptionDTO.class, new Class<?>[]{long.class, String.class, String.class}, productOptionId, color, size);
    }

}

