package com.impacus.maketplace.dto.category.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
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
        this.subCategories = validateSubCategories(subCategories) ? subCategories : new ArrayList<>();
    }

    public boolean validateSubCategories(List<SubCategoryDetailDTO> subCategories) {
        if (subCategories.size() == 1) {
            SubCategoryDetailDTO subCategoryDetailDTO = subCategories.get(0);

            if (subCategoryDetailDTO.getSubCategoryId() == null && subCategoryDetailDTO.getSubCategoryName() == null) {
                return false;
            }
        }

        return true;
    }

    public void setSubCategories(List<SubCategoryDetailDTO> subCategories) {
        this.subCategories = subCategories;
    }
}
