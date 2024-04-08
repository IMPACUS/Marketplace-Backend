package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameRequest;
import com.impacus.maketplace.dto.category.request.SubCategoryRequest;
import com.impacus.maketplace.dto.category.request.SuperCategoryRequest;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDTO;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.category.SuperCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ApiResponseEntity<Object> addSuperCategory(
            @Valid @RequestBody SuperCategoryRequest superCategoryRequest) {
        SuperCategoryDTO superCategoryDTO = superCategoryService.addSuperCategory(superCategoryRequest);
        return ApiResponseEntity
                .builder()
                .data(superCategoryDTO)
                .build();
    }

    /**
     * 2차 카테고리 추가 API
     *
     * @param thumbnail
     * @param subCategoryRequest
     * @return
     */
    @PostMapping("admin/sub-category")
    public ApiResponseEntity<Object> addSubCategory(
            @RequestPart(value = "sub-category-thumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "sub-category") SubCategoryRequest subCategoryRequest) {
        SubCategoryDTO subCategoryDTO = subCategoryService.addSubCategory(thumbnail, subCategoryRequest);
        return ApiResponseEntity
                .builder()
                .data(subCategoryDTO)
                .build();
    }

    /**
     * 1차 카테고리명 수정 API
     *
     * @param categoryId
     * @param categoryNameRequest
     * @return
     */
    @PutMapping("admin/super-category/{categoryId}")
    public ApiResponseEntity<Object> updateSuperCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @Valid @RequestBody ChangeCategoryNameRequest categoryNameRequest) {
        SuperCategoryDTO superCategoryDTO = superCategoryService.updateSuperCategory(categoryId, categoryNameRequest);
        return ApiResponseEntity
                .builder()
                .data(superCategoryDTO)
                .build();
    }

    /**
     * 2차 카테고리명 수정 API
     *
     * @param categoryId
     * @param categoryNameRequest
     * @return
     */
    @PutMapping("admin/sub-category/{categoryId}")
    public ApiResponseEntity<Object> updateSubCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestPart(value = "sub-category-thumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "sub-category") ChangeCategoryNameRequest subCategoryRequest) {
        SubCategoryDTO subCategoryDTO = subCategoryService.updateSubCategory(categoryId, thumbnail, subCategoryRequest);
        return ApiResponseEntity
                .builder()
                .data(subCategoryDTO)
                .build();
    }

    @GetMapping("")
    public ApiResponseEntity<Object> getAllCategory() {
        List<CategoryDetailDTO> categoryDTOs = superCategoryService.findAllCategory();
        return ApiResponseEntity
                .builder()
                .data(categoryDTOs)
                .build();
    }

    /**
     * 2차 카테고리 삭제 API
     *
     * @return
     */
    @DeleteMapping("admin/sub-category")
    public ApiResponseEntity<Object> deleteSubCategory(
            @RequestParam(name = "subCategoryId") List<Long> subCategoryIdList) {
        subCategoryService.deleteSubCategory(subCategoryIdList);
        return ApiResponseEntity
                .builder()
                .build();
    }

    /**
     * 1차 카테고리 삭제 API
     *
     * @return
     */
    @DeleteMapping("admin/super-category")
    public ApiResponseEntity<Object> deleteSuperCategory(
            @RequestParam(name = "superCategoryId") List<Long> superCategoryIdList) {
        subCategoryService.deleteSuperCategory(superCategoryIdList);
        return ApiResponseEntity
                .builder()
                .build();
    }
}
