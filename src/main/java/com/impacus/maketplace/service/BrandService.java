package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.Brand;
import com.impacus.maketplace.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;

    /**
     * BrandId로 Brand를 찾는 함수
     *
     * @param brandId
     * @return
     */
    public Brand findBrandById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_BRAND));
    }
}
