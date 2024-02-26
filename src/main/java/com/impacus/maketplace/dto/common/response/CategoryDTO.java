package com.impacus.maketplace.dto.common.response;

import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.enumType.category.SuperCategory;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private SuperCategory superCategory;
    private List<SubCategory> subCategory;

    public CategoryDTO(SuperCategory superCategory, List<SubCategory> categoryList) {
        this.superCategory = superCategory;
        this.subCategory = categoryList;
    }
}
