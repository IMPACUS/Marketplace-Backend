package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductService {
    /**
     * TemporaryProduct 데이터가 사용자에게 등록되어 있는지 확인하는 함수
     *
     * @param userId
     * @return
     */
    public IsExistedTemporaryProductDTO checkIsExistedTemporaryProduct(Long userId) {
        return null;
    }
}
