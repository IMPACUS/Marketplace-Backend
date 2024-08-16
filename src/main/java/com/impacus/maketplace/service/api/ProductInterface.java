package com.impacus.maketplace.service.api;

import com.impacus.maketplace.common.exception.CustomException;

/**
 * 상품 관련 인터페이스
 *
 * @author 김용현
 */
public interface ProductInterface {

    /**
     * 상품 ID에 대해 상품이 존재하는지 확인
     *
     * @param productId 상품 ID
     * @throws CustomException 존재하지 않으면 예외 발생하게 구현
     */
    void checkExistenceById(long productId);

}
