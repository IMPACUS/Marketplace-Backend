package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.entity.seller.Brand;
import com.impacus.maketplace.repository.seller.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;

    /**
     * Brand 저장 함수
     *
     * @param brand
     */
    @Transactional
    public void saveBrand(Brand brand) {
        brandRepository.save(brand);
    }
}
