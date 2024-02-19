package com.impacus.maketplace.service.common;

import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.enumType.category.SuperCategory;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.common.response.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnumService {

    public List<CategoryDTO> getAllCategory() {
        try {
            return SuperCategory.getAllSuperCategory().stream()
                    .map(superCategory -> {
                        List<SubCategory> subCategoryList = SubCategory.getSubCategoryBySuperCategory(superCategory);

                        return new CategoryDTO(superCategory, subCategoryList);
                    }).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new CustomException(exception);
        }
    }
}
