package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.DetailStepProductDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;

public interface TemporaryProductService {

    /**
     * TemporaryProduct 데이터가 사용자에게 등록되어 있는지 확인
     *
     * @param userId 등록되어 있는지 확인하는 사용자
     * @return 임시 상품 데이터 존재 여부
     */
    IsExistedTemporaryProductDTO checkIsExistedTemporaryProduct(Long userId);

    /**
     * 임시 저장된 기본 상품 데이터를 추가하거나 수정
     *
     * @param userId 상품을 등록하거나 수정하는 사용자의 ID
     * @param dto    기본 상품 데이터가 포함된 DTO 객체
     */
    void addOrModifyTemporaryProductAtBasic(Long userId, BasicStepProductDTO dto);

    /**
     * 임시 저장된 상품의 옵션 데이터를 추가하거나 수정
     *
     * @param userId 상품 옵션을 등록하거나 수정하는 사용자의 ID
     * @param dto    상품 옵션 데이터가 포함된 DTO 객체
     */
    void addOrModifyTemporaryProductAtOptions(Long userId, OptionStepProductDTO dto);

    /**
     * 임시 저장된 상품의 상세 데이터를 추가하거나 수정
     *
     * @param userId 상품 상세 정보를 등록하거나 수정하는 사용자의 ID
     * @param dto    상품 상세 정보가 포함된 DTO 객체
     */
    void addOrModifyTemporaryProductAtDetails(Long userId, DetailStepProductDTO dto);

    /**
     * userId로 TemporaryProduct 조회
     *
     * @param userId 사용자 아이디
     * @return TemporaryProduct
     */
    TemporaryProduct findTemporaryProductByUserId(Long userId);

    /**
     * TemporaryProduct 삭제
     * 1. TemporaryProductOption 삭제
     * 2. TemporaryProductDescription 이미지 삭제
     * 3. TemporaryProductDescription 삭제
     * 4. TemporaryProduct 대표 이미지 삭제
     * 5. TemporaryProduct 삭제
     *
     * @param userId 사용자 아이디
     */
    void deleteTemporaryProduct(Long userId);


    /**
     * TemporaryProduct 를 조회하는 함수
     *
     * @param userId 사용자 아이디
     * @return 임시 상품 데이터
     */
    ProductDetailForWebDTO findTemporaryProduct(Long userId);
}
