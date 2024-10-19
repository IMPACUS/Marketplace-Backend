package com.impacus.maketplace.dto.product.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.product.response.QWebProductOptionDTO is a Querydsl Projection type for WebProductOptionDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebProductOptionDTO extends ConstructorExpression<WebProductOptionDTO> {

    private static final long serialVersionUID = 226447726L;

    public QWebProductOptionDTO(com.querydsl.core.types.Expression<Long> productOptionId, com.querydsl.core.types.Expression<String> color, com.querydsl.core.types.Expression<String> size) {
        super(WebProductOptionDTO.class, new Class<?>[]{long.class, String.class, String.class}, productOptionId, color, size);
    }

}

