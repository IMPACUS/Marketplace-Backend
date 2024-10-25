package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QProductOptionDetailDTO is a Querydsl Projection type for ProductOptionDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductOptionDetailDTO extends ConstructorExpression<ProductOptionDetailDTO> {

    private static final long serialVersionUID = -1423097717L;

    public QProductOptionDetailDTO(com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size, com.querydsl.core.types.Expression<Long> stock) {
        super(ProductOptionDetailDTO.class, new Class<?>[]{long.class, String.class, String.class, long.class}, productOptionId, color, size, stock);
    }

}

