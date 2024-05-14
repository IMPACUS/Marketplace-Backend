package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.enumType.error.CategoryEnum;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameDTO;
import com.impacus.maketplace.dto.category.request.CreateSubCategoryDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDTO;
import com.impacus.maketplace.entity.category.SubCategory;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.entity.common.AttachFile;
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
    private static final int THUMBNAIL_SIZE_LIMIT = 10800; // 60 픽셀 * 60픽셀
    private static final int MAX_SUBCATEGORY_CNT = 20;
    private static final String THUMBNAIL_IMAGE_DIRECTORY = "subCategoryThumbnail";
    private final SubCategoryRepository subCategoryRepository;
    private final SuperCategoryService superCategoryService;
    private final AttachFileService attachFileService;
    private final ObjectCopyHelper objectCopyHelper;
    private final ProductRepository productRepository;

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
                throw new CustomException(CommonErrorType.DUPLICATED_SUB_CATEGORY);
            }

            // 2. 1차 카테고리 존재 확인
            if (!superCategoryService.existsBySuperCategoryId(superCategoryId)) {
                throw new CustomException(CategoryEnum.NOT_EXISTED_SUPER_CATEGORY);
            } else {
                if (countBySuperCategoryId(superCategoryId) > MAX_SUBCATEGORY_CNT) {
                    throw new CustomException(CategoryEnum.EXCEED_MAX_SUB_CATEGORY);
                }
            }

            // 3. 썸네일 용량 확인 & 저장
            AttachFile attachFile = null;
            if (thumbnail.getSize() > THUMBNAIL_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_THUMBNAIL, "2차 카테고리 이미지 크기가 제한 사이즈보다 큽니다.");
            } else {
                attachFile = attachFileService.uploadFileAndAddAttachFile(thumbnail, THUMBNAIL_IMAGE_DIRECTORY);
            }

            // 3. 2차 카테고리 저장
            SubCategory subCategory = subCategoryRepository.save(
                    new SubCategory(superCategoryId, attachFile.getId(), subCategoryName)
            );

            return objectCopyHelper.copyObject(subCategory, SubCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
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
                throw new CustomException(CommonErrorType.DUPLICATED_SUB_CATEGORY);
            }

            // 2. category 찾기
            SubCategory subCategory = findBySubCategoryId(categoryId);

            // 3. 썸네일 용량 확인 & 저장
            if (thumbnail.getSize() > THUMBNAIL_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_THUMBNAIL, "2차 카테고리 이미지 크기가 제한 사이즈보다 큽니다.");
            } else {
                attachFileService.updateAttachFile(subCategory.getThumbnailId(), thumbnail, THUMBNAIL_IMAGE_DIRECTORY);
            }

            // 4. 내용 업데이트
            subCategoryRepository.updateCategoryName(categoryId, subCategoryName);
            return objectCopyHelper.copyObject(subCategory, SubCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
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
                .orElseThrow(() -> new CustomException(CategoryEnum.NOT_EXISTED_SUB_CATEGORY));
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
                    throw new CustomException(CategoryEnum.CANNOT_DELETE_SUB_CATEGORY_WITH_PRODUCT);
                }

                SubCategory subCategory = findBySubCategoryId(subCategoryId);
                subCategories.add(subCategory);
            }

            // 2. 삭제
            subCategoryRepository.deleteAllInBatch(subCategories);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 1차 카테고리 다중 삭제
     *
     * @param superCategoryIdList
     * @return
     */
    @Transactional
    public void deleteSuperCategory(List<Long> superCategoryIdList) {
        try {
            // 1. 2차 카테고리 존재 확인
            List<SuperCategory> superCategories = new ArrayList<>();
            List<SubCategory> subCategories = new ArrayList<>();
            for (Long superCategoryId : superCategoryIdList) {
                if (productRepository.existsBySuperCategoryId(superCategoryId)) {
                    throw new CustomException(CategoryEnum.CANNOT_DELETE_SUPER_CATEGORY_WITH_PRODUCT);
                }

                SuperCategory superCategory = superCategoryService.findBySuperCategoryId(superCategoryId);
                subCategories.addAll(subCategoryRepository.findBySuperCategoryId(superCategoryId));
                superCategories.add(superCategory);
            }

            // 2. 삭제
            subCategoryRepository.deleteAllInBatch(subCategories);
            superCategoryService.deleteAllInBatch(superCategories);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
