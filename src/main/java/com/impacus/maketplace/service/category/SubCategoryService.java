package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameDTO;
import com.impacus.maketplace.dto.category.request.CreateSubCategoryDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDTO;
import com.impacus.maketplace.entity.category.SubCategory;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.redis.service.ProductSearchService;
import com.impacus.maketplace.repository.category.SubCategoryRepository;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubCategoryService {
    private static final int MAX_SUBCATEGORY_CNT = 20;

    private final SubCategoryRepository subCategoryRepository;
    private final SuperCategoryService superCategoryService;
    private final AttachFileService attachFileService;
    private final ObjectCopyHelper objectCopyHelper;
    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;

    /**
     * 2차 카테고리 추가하는 함수
     *
     * @param thumbnail
     * @param subCategoryRequest
     * @return
     */
    @Transactional
    public SubCategoryDTO addSubCategory(MultipartFile thumbnail, CreateSubCategoryDTO subCategoryRequest) {
        try {
            String subCategoryName = subCategoryRequest.getName();
            Long superCategoryId = subCategoryRequest.getSuperCategoryId();

            // 1. 중복된 2차 카테고리 명 확인
            if (existsBySuperCategoryName(subCategoryName)) {
                throw new CustomException(CategoryErrorType.DUPLICATED_SUB_CATEGORY);
            }

            // 2. 1차 카테고리 존재 확인
            if (!superCategoryService.existsBySuperCategoryId(superCategoryId)) {
                throw new CustomException(CategoryErrorType.NOT_EXISTED_SUPER_CATEGORY);
            } else {
                if (countBySuperCategoryId(superCategoryId) > MAX_SUBCATEGORY_CNT) {
                    throw new CustomException(CategoryErrorType.EXCEED_MAX_SUB_CATEGORY);
                }
            }

            // 3. 썸네일 용량 확인 & 저장
            AttachFile attachFile = null;
            if (thumbnail.getSize() > FileSizeConstants.THUMBNAIL_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_THUMBNAIL, "2차 카테고리 이미지 크기가 제한 사이즈보다 큽니다.");
            } else {
                attachFile = attachFileService.uploadFileAndAddAttachFile(thumbnail, DirectoryConstants.THUMBNAIL_IMAGE_DIRECTORY);
            }

            // 4. 2차 카테고리 저장
            SubCategory subCategory = subCategoryRepository.save(
                    new SubCategory(superCategoryId, attachFile.getId(), subCategoryName)
            );

            // 5. 2차 카테고리 검색어 저장
            addSubCategorySearchData(subCategory);

            return objectCopyHelper.copyObject(subCategory, SubCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 2차 카테고리 검색어 저장
     *
     * @param subCategory 저장할 2차 카테고리
     */
    @Transactional
    public void addSubCategorySearchData(SubCategory subCategory) {
        try {
            productSearchService.addSearchData(
                    SearchType.SUBCATEGORY,
                    subCategory.getId(),
                    subCategory.getName()
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("addSubCategorySearchData", "Fail to add search data", e);
        }
    }

    /**
     * 2차 카테고리에 중복된 명이 존재하는지 확인하는 함수
     *
     * @param name
     * @return
     */
    private boolean existsBySuperCategoryName(String name) {
        return subCategoryRepository.existsByName(name);
    }

    /**
     * 2차 카테고리 명 수정 함수
     * @param thumbnail
     * @param categoryNameRequest
     * @return
     */
    @Transactional
    public SubCategoryDTO updateSubCategory(MultipartFile thumbnail, ChangeCategoryNameDTO categoryNameRequest) {
        try {
            Long categoryId = categoryNameRequest.getCategoryId();
            String subCategoryName = categoryNameRequest.getName();

            // 1. 중복된 2차 카테고리 명 확인
            if (existsBySuperCategoryName(subCategoryName)) {
                throw new CustomException(CategoryErrorType.DUPLICATED_SUB_CATEGORY);
            }

            // 2. category 찾기
            SubCategory subCategory = findBySubCategoryId(categoryId);

            // 3. 썸네일 용량 확인 & 저장
            if (thumbnail.getSize() > FileSizeConstants.THUMBNAIL_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_THUMBNAIL, "2차 카테고리 이미지 크기가 제한 사이즈보다 큽니다.");
            } else {
                attachFileService.updateAttachFile(
                        subCategory.getThumbnailId(),
                        thumbnail,
                        DirectoryConstants.THUMBNAIL_IMAGE_DIRECTORY
                );
            }

            // 4. 내용 업데이트
            subCategoryRepository.updateCategoryName(categoryId, subCategoryName);

            // 5. 검색어 데이터 수정
            updateSubCategorySearchData(categoryId, subCategoryName);

            return objectCopyHelper.copyObject(subCategory, SubCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 2차 카테고리 검색어 수정
     *
     * @param subCategoryId 수정할 2차 카테고리 ID
     * @param name          2차 카테고리 명
     */
    @Transactional
    public void updateSubCategorySearchData(Long subCategoryId, String name) {
        try {
            productSearchService.updateSearchData(
                    SearchType.SUBCATEGORY,
                    subCategoryId,
                    name
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("updateSubCategorySearchData", "Fail to update search data", e);
        }
    }

    /**
     * id로 SubCategory 를 찾는 함수
     *
     * @param id
     * @return
     */
    public SubCategory findBySubCategoryId(Long id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY));
    }


    /**
     * 2차 카테고리에 id이 존재하는지 확인하는 함수
     *
     * @param id
     * @return
     */
    public boolean existsBySubCategoryId(Long id) {
        return subCategoryRepository.existsById(id);
    }

    private int countBySuperCategoryId(Long superCategoryId) {
        return subCategoryRepository.countBySuperCategoryId(superCategoryId);
    }

    /**
     * 2차 카테고리 다중 삭제
     *
     * @param subCategoryIdList
     * @return
     */
    @Transactional
    public void deleteSubCategory(List<Long> subCategoryIdList) {
        try {
            // 1. 2차 카테고리 존재 확인
            List<SubCategory> subCategories = new ArrayList<>();
            for (Long subCategoryId : subCategoryIdList) {
                if (productRepository.existsByCategoryId(subCategoryId)) {
                    throw new CustomException(CategoryErrorType.CANNOT_DELETE_SUB_CATEGORY_WITH_PRODUCT);
                }

                SubCategory subCategory = findBySubCategoryId(subCategoryId);
                subCategories.add(subCategory);
            }

            // 2. 삭제
            deleteAllSubCategory(subCategories);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 1차 카테고리 다중 삭제
     *
     * @param superCategoryIds
     * @return
     */
    @Transactional
    public void deleteSuperCategory(List<Long> superCategoryIds) {
        try {
            // 1. 2차 카테고리 존재 확인
            List<SuperCategory> superCategories = new ArrayList<>();
            List<SubCategory> subCategories = new ArrayList<>();
            for (Long superCategoryId : superCategoryIds) {
                if (productRepository.existsBySuperCategoryId(superCategoryId)) {
                    throw new CustomException(CategoryErrorType.CANNOT_DELETE_SUPER_CATEGORY_WITH_PRODUCT);
                }

                SuperCategory superCategory = superCategoryService.findBySuperCategoryId(superCategoryId);
                subCategories.addAll(subCategoryRepository.findBySuperCategoryId(superCategoryId));
                superCategories.add(superCategory);
            }

            // 2. 삭제
            deleteAllSubCategory(subCategories);
            superCategoryService.deleteAllInBatch(superCategories);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 2차 카테고리 다중 삭제
     *
     * @param subCategories 삭제할 2차 카테고리
     */
    @Transactional
    public void deleteAllSubCategory(List<SubCategory> subCategories) {
        // 1. 2차 카테고리 삭제
        subCategoryRepository.deleteAllInBatch(subCategories);

        // 2. 2차 카테고리 검색어 삭제
        subCategories.forEach(x -> deleteSubCategorySearchData(x.getId()));
    }

    /**
     * 2챠 카테고리 검색어 삭제
     *
     * @param subCategoryId 삭제할 2차 카테고리 ID
     */
    @Transactional
    public void deleteSubCategorySearchData(Long subCategoryId) {
        try {
            productSearchService.deleteSearchData(
                    SearchType.SUBCATEGORY,
                    subCategoryId
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("deleteSubCategorySearchData", "Fail to delete search data", e);
        }
    }
}
