package com.impacus.maketplace.dto.category.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.category.response.QCategoryDetailDTO is a Querydsl Projection type for CategoryDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCategoryDetailDTO extends ConstructorExpression<CategoryDetailDTO> {

    private static final long serialVersionUID = 1864057734L;

    public QCategoryDetailDTO(com.querydsl.core.types.Expression<Long> superCategoryId, com.querydsl.core.types.Expression<String> superCategoryName, com.querydsl.core.types.Expression<? extends java.util.List<SubCategoryDetailDTO>> subCategories) {
        super(CategoryDetailDTO.class, new Class<?>[]{long.class, String.class, java.util.List.class}, superCategoryId, superCategoryName, subCategories);
    }

}

