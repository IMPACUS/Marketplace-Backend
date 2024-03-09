package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.SuperCategoryRequest;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuperCategoryService {

    private final SuperCategoryRepository superCategoryRepository;
    private final ObjectCopyHelper objectCopyHelper;

    // 1차 카테고리를 추가하는 API
    @Transactional
    public SuperCategoryDTO addSuperCategory(SuperCategoryRequest superCategoryRequest) {
        try {
            String superCategoryName = superCategoryRequest.getName();

            // 1. 중복된 1차 카테고리 명 확인
            if (existsBySuperCategoryName(superCategoryName)) {
                throw new CustomException(ErrorType.DUPLICATED_SUPER_CATEGORY);
            }

            // 2. 1차 카테고리 저장
            SuperCategory superCategory = superCategoryRepository.save(new SuperCategory(superCategoryName));

            return objectCopyHelper.copyObject(superCategory, SuperCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 1차 카테고리에 중복된 명이 존재하는지 확인하는 함수
     *
     * @param name
     * @return
     */
    private boolean existsBySuperCategoryName(String name) {
        return superCategoryRepository.existsByName(name);
    }
}
