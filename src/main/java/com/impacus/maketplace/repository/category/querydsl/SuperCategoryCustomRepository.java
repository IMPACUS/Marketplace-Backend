package com.impacus.maketplace.repository.category.querydsl;

import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;

import java.util.List;

public interface SuperCategoryCustomRepository {
    List<CategoryDetailDTO> findAllCategory();
}
