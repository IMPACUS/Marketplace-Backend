package com.impacus.maketplace.repository.category.querydsl;

import com.impacus.maketplace.dto.category.dto.SearchCategoryDTO;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SuperCategoryCustomRepository {
    List<CategoryDetailDTO> findSuperCategories(String keyword);

    Slice<SearchCategoryDTO> findSuperCategoriesBy(PageRequest of);

    Slice<SearchCategoryDTO> findSubCategoriesBy(PageRequest of);
}
