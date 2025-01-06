package com.impacus.maketplace.service.category;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.category.request.ChangeCategoryNameDTO;
import com.impacus.maketplace.dto.category.request.CreateSuperCategoryDTO;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SuperCategoryDTO;
import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.redis.service.SearchProductService;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuperCategoryService {

    private static final String BRAND_CATEGORY_NAME = "브랜드";

    private final SuperCategoryRepository superCategoryRepository;
    private final ObjectCopyHelper objectCopyHelper;
    private final ReadSellerService readSellerService;
    private final SearchProductService searchProductService;

    /**
     * 1차 카테고리 추가하는 함수
     *
     * @param superCategoryRequest The request object containing the details of the super category to be added, including its name.
     * @return A DTO representing the newly added super category, including its ID and name.
     */
    @Transactional
    public SuperCategoryDTO addSuperCategory(CreateSuperCategoryDTO superCategoryRequest) {
        try {
            String superCategoryName = superCategoryRequest.getName();

            // 1. 중복된 1차 카테고리 명 확인
            if (existsBySuperCategoryName(superCategoryName)) {
                throw new CustomException(CategoryErrorType.DUPLICATED_SUPER_CATEGORY);
            }

            // 2. 1차 카테고리 저장
            SuperCategory superCategory = superCategoryRepository.save(new SuperCategory(superCategoryName));

            // 3. 1차 카테고리 검색어 저장
            addSuperCategorySearchData(superCategory);

            return objectCopyHelper.copyObject(superCategory, SuperCategoryDTO.class);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 1차 카테고리 검색어 저장
     *
     * @param superCategory 저장할 1차 카테고리
     */
    @Transactional
    public void addSuperCategorySearchData(SuperCategory superCategory) {
        try {
            searchProductService.addSearchData(
                    SearchType.CATEGORY,
                    superCategory.getId(),
                    superCategory.getName()
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("addSuperCategorySearchData", "Fail to add search data", e);
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
     * @param dto
     * @return
     */
    @Transactional
    public Boolean updateSuperCategory(ChangeCategoryNameDTO dto) {
        try {
            Long categoryId = dto.getCategoryId();
            String superCategoryName = dto.getName();

            // 1. 중복된 1차 카테고리 명 확인
            if (existsBySuperCategoryName(superCategoryName)) {
                throw new CustomException(CategoryErrorType.DUPLICATED_SUPER_CATEGORY);
            }

            // 2. 업데이트
            int rowCnt = superCategoryRepository.updateCategoryNameById(categoryId, superCategoryName);
            if (rowCnt == 0) {
                throw new CustomException(CategoryErrorType.NOT_EXISTED_SUPER_CATEGORY);
            }

            // 3. 검색어 데이터 수정
            updateSuperCategorySearchData(categoryId, superCategoryName);
            return true;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 1차 카테고리 검색어 수정
     *
     * @param superCategoryId 수정할 1차 카테고리 ID
     * @param name            1차 카테고리 명
     */
    @Transactional
    public void updateSuperCategorySearchData(Long superCategoryId, String name) {
        // revision by shin
        String oldSearchName = "";
        String newSearchName = "";
        try {
            searchProductService.updateSearchData(
                    SearchType.CATEGORY,
                    superCategoryId,
//                    name,
                    oldSearchName,
                    oldSearchName
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("updateSuperCategorySearchData", "Fail to update search data", e);
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
                .orElseThrow(() -> new CustomException(CategoryErrorType.NOT_EXISTED_SUPER_CATEGORY));
    }

    public List<CategoryDetailDTO> findAllCategory(boolean isExceptBrand) {
        return findAllCategory(isExceptBrand, null);
    }

    /**
     * 전체 1차/2차 카테고리를 찾는 함수
     * - true인 경우 brand 데이터 조회, false인 경우 brand까지 포함한 전체 데이터 조회
     *
     * @param isExceptBrand brand 데이터 포함 여부
     * @param keyword       2차 카테고리 검색어
     * @return
     */
    public List<CategoryDetailDTO> findAllCategory(boolean isExceptBrand, String keyword) {
        List<CategoryDetailDTO> dtos = superCategoryRepository.findAllCategory(keyword);

        // 2. brand 데이터 삭제 여부 확인
        int brandSuperCategoryId = -1;
        for (int i = 0; i < dtos.size(); i++) {
            CategoryDetailDTO category = dtos.get(i);
            if (category.getSuperCategoryName().equals(BRAND_CATEGORY_NAME)) {
                brandSuperCategoryId = i;
                break;
            }
        }
        if (isExceptBrand) {
            // 2-1. brand 데이터 삭제
            dtos.remove(brandSuperCategoryId);
        } else {
            // 2-2. brand 브랜드 명을 2차 카테고리에 추가
            List<SubCategoryDetailDTO> subCategories = readSellerService.findAllBrandName();
            dtos.get(brandSuperCategoryId).setSubCategories(subCategories);
        }

        return dtos;
    }

    /**
     * 1차 카테고리 다중 삭제 함수
     *
     * @param superCategories 삭제할 1차 카테고리 리스트
     */
    @Transactional
    public void deleteAllInBatch(List<SuperCategory> superCategories) {
        // 1. 1차 카테고리 삭제
        superCategoryRepository.deleteAllInBatch(superCategories);

        // 2. 검색어 삭제
        superCategories.forEach(x -> deleteSuperCategorySearchData(x.getId(), x.getName()));
    }

    /**
     * 1챠 카테고리 검색어 삭제
     *
     * @param superCategoryId 삭제할 1차 카테고리 ID
     */
    @Transactional // revision by shin
    public void deleteSuperCategorySearchData(Long superCategoryId, String name) {
        try {
            searchProductService.deleteSearchData(
                    SearchType.CATEGORY,
                    superCategoryId,
                    name
            );
        } catch (Exception e) {
            LogUtils.writeErrorLog("deleteSuperCategorySearchData", "Fail to delete search data", e);
        }
    }
}
