package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameRequest;
import com.impacus.maketplace.dto.category.request.SuperCategoryRequest;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuperCategoryService {

    private final SuperCategoryRepository superCategoryRepository;
    private final ObjectCopyHelper objectCopyHelper;

    /**
     * 1차 카테고리 추가하는 함수
     *
     * @param superCategoryRequest
     * @return
     */
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

    /**
     * 1차 카테고리에 id이 존재하는지 확인하는 함수
     *
     * @param id
     * @return
     */
    public boolean existsBySuperCategoryId(Long id) {
        return superCategoryRepository.existsById(id);
    }

    /**
     * 1차 카테고리 명 수정 함수
     *
     * @param categoryId
     * @param categoryNameRequest
     * @return
     */
    @Transactional
    public SuperCategoryDTO updateSuperCategory(Long categoryId, ChangeCategoryNameRequest categoryNameRequest) {
        try {
            SuperCategory superCategory = findBySuperCategoryId(categoryId);

            superCategory.setName(categoryNameRequest.getName());
            superCategoryRepository.save(superCategory);
            return objectCopyHelper.copyObject(superCategory, SuperCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * id로 SuperCategory 를 찾는 함수
     *
     * @param id
     * @return
     */
    public SuperCategory findBySuperCategoryId(Long id) {
        return superCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_SUPER_CATEGORY));
    }

    /**
     * 전체 1차/2차 카테고리를 찾는 함수
     *
     * @return
     */
    public List<CategoryDetailDTO> findAllCategory() {
        return superCategoryRepository.findAllCategory();
    }

    public void deleteAllInBatch(List<SuperCategory> superCategories) {
        superCategoryRepository.deleteAllInBatch(superCategories);
    }
}
