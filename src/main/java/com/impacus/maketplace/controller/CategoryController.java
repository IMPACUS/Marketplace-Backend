package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameDTO;
import com.impacus.maketplace.dto.category.request.CreateSubCategoryDTO;
import com.impacus.maketplace.dto.category.request.CreateSuperCategoryDTO;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDTO;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.category.SuperCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PostMapping("/super-category")
    public ApiResponseEntity<SuperCategoryDTO> addSuperCategory(
            @Valid @RequestBody CreateSuperCategoryDTO superCategoryRequest) {

        SuperCategoryDTO superCategoryDTO = superCategoryService.addSuperCategory(superCategoryRequest);
        return ApiResponseEntity
                .<SuperCategoryDTO>builder()
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
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PostMapping("sub-category")
    public ApiResponseEntity<SubCategoryDTO> addSubCategory(
            @RequestPart(value = "subCategoryThumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "subCategory") CreateSubCategoryDTO subCategoryRequest) {
        SubCategoryDTO subCategoryDTO = subCategoryService.addSubCategory(thumbnail, subCategoryRequest);
        return ApiResponseEntity
                .<SubCategoryDTO>builder()
                .data(subCategoryDTO)
                .build();
    }

    /**
     * 1차 카테고리명 수정 API
     *
     * @param categoryNameRequest
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PutMapping("super-category")
    public ApiResponseEntity<Boolean> updateSuperCategory(
            @Valid @RequestBody ChangeCategoryNameDTO categoryNameRequest) {
        Boolean result = superCategoryService.updateSuperCategory(categoryNameRequest);
        return ApiResponseEntity.<Boolean>builder()
                .data(result)
                .build();
    }

    /**
     * 2차 카테고리명 수정 API
     * @param thumbnail
     * @param subCategoryRequest
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PutMapping("sub-category/{categoryId}")
    public ApiResponseEntity<SubCategoryDTO> updateSubCategory(
            @RequestPart(value = "subCategoryThumbnail", required = false) MultipartFile thumbnail,
            @Valid @RequestPart(value = "subCategory") ChangeCategoryNameDTO subCategoryRequest) {
        SubCategoryDTO subCategoryDTO = subCategoryService.updateSubCategory(thumbnail, subCategoryRequest);
        return ApiResponseEntity
                .<SubCategoryDTO>builder()
                .data(subCategoryDTO)
                .build();
    }

    /**
     * 전체 카테고리 조회 API
     * @return
     */
    @GetMapping("")
    public ApiResponseEntity<List<CategoryDetailDTO>> getAllCategory() {
        List<CategoryDetailDTO> categoryDTOs = superCategoryService.findAllCategory(true);
        return ApiResponseEntity
                .<List<CategoryDetailDTO>>builder()
                .data(categoryDTOs)
                .build();
    }

    /**
     * 상품 등록/수정용 전체 카테고리 조회 API (상품 등록/수정용인 경우 브랜드 데이터가 제외됨)
     * @return
     */
    @GetMapping("")
    public ApiResponseEntity<List<CategoryDetailDTO>> getAllCategoryForProduct() {
        List<CategoryDetailDTO> categoryDTOs = superCategoryService.findAllCategory(false);
        return ApiResponseEntity
                .<List<CategoryDetailDTO>>builder()
                .data(categoryDTOs)
                .build();
    }

    /**
     * 2차 카테고리 삭제 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @DeleteMapping("sub-category")
    public ApiResponseEntity<Boolean> deleteSubCategory(
            @RequestParam(name = "sub-category-id") List<Long> subCategoryIdList) {
        subCategoryService.deleteSubCategory(subCategoryIdList);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * 1차 카테고리 삭제 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @DeleteMapping("super-category")
    public ApiResponseEntity<Boolean> deleteSuperCategory(
            @RequestParam(name = "super-category-id") List<Long> superCategoryIdList) {
        subCategoryService.deleteSuperCategory(superCategoryIdList);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }
}
