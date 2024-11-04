package com.impacus.maketplace.dto.category.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.category.response.QSubCategoryDetailDTO is a Querydsl Projection type for SubCategoryDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSubCategoryDetailDTO extends ConstructorExpression<SubCategoryDetailDTO> {

    private static final long serialVersionUID = -516362438L;

    public QSubCategoryDetailDTO(com.querydsl.core.types.Expression<Long> subCategoryId, com.querydsl.core.types.Expression<String> subCategoryName, com.querydsl.core.types.Expression<String> thumbnailUrl) {
        super(SubCategoryDetailDTO.class, new Class<?>[]{long.class, String.class, String.class}, subCategoryId, subCategoryName, thumbnailUrl);
    }

}

