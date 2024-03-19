package com.impacus.maketplace.dto.category.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDetailDTO {
    private Long superCategoryId;
    private String superCategoryName;
    private List<SubCategoryDetailDTO> subCategories;

    @QueryProjection
    public CategoryDetailDTO(Long superCategoryId,
                             String superCategoryName,
                             List<SubCategoryDetailDTO> subCategories) {
        this.superCategoryId = superCategoryId;
        this.superCategoryName = superCategoryName;
        this.subCategories = subCategories;
    }
}
