package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.SubCategoryRequest;
import com.impacus.maketplace.dto.category.response.SubCategoryDTO;
import com.impacus.maketplace.entity.category.SubCategory;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.repository.category.SubCategoryRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubCategoryService {
    private static final int THUMBNAIL_SIZE_LIMIT = 10800; // 60 픽셀 * 60픽셀
    private static final String THUMBNAIL_IMAGE_DIRECTORY = "subCategoryThumbnail";
    private final SubCategoryRepository subCategoryRepository;
    private final SuperCategoryService superCategoryService;
    private final AttachFileService attachFileService;
    private final ObjectCopyHelper objectCopyHelper;

    /**
     * 2차 카테고리 추가하는 함수
     *
     * @param thumbnail
     * @param subCategoryRequest
     * @return
     */
    @Transactional
    public SubCategoryDTO addSubCategory(MultipartFile thumbnail, SubCategoryRequest subCategoryRequest) {
        try {
            String subCategoryName = subCategoryRequest.getName();
            Long superCategoryId = subCategoryRequest.getSuperCategoryId();

            // 1. 중복된 2차 카테고리 명 확인
            if (existsBySuperCategoryName(subCategoryName)) {
                throw new CustomException(ErrorType.DUPLICATED_SUB_CATEGORY);
            }

            // 2. 1차 카테고리 존재 확인
            if (!superCategoryService.existsBySuperCategoryId(superCategoryId)) {
                throw new CustomException(ErrorType.NOT_EXISTED_SUPER_CATEGORY);
            }

            // 3. 썸네일 용량 확인 & 저장
            AttachFile attachFile = null;
            if (thumbnail.getSize() > THUMBNAIL_SIZE_LIMIT) {
                throw new CustomException(ErrorType.INVALID_THUMBNAIL, "2차 카테고리 이미지 크기가 제한 사이즈보다 큽니다.");
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
}
