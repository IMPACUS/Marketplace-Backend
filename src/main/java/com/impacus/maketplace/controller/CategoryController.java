package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.category.request.SuperCategoryRequest;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.category.SuperCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryController {
    private final SuperCategoryService superCategoryService;
    private final SubCategoryService subCategoryService;

    /**
     * 1차 카테고리 추가 API
     *
     * @param superCategoryRequest
     * @return
     */
    @PostMapping("admin/super-category")
    public ApiResponseEntity<Object> addWishlist(
            @Valid @RequestBody SuperCategoryRequest superCategoryRequest) {
        SuperCategoryDTO superCategoryDTO = superCategoryService.addSuperCategory(superCategoryRequest);
        return ApiResponseEntity
                .builder()
                .data(superCategoryDTO)
                .build();
    }
}
