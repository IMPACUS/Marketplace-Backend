package com.impacus.maketplace.service.product;

import com.impacus.maketplace.entity.product.ProductDescription;
import com.impacus.maketplace.repository.ProductDescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductDescriptionService {

    private final ProductDescriptionRepository productDescriptionRepository;

    /**
     * ProductDescription을 저장하는 함수
     *
     * @param productId
     * @param description
     */
    @Transactional
    public ProductDescription addProductDescription(Long productId, String description) {
        ProductDescription newProductDescription = ProductDescription.builder()
                .productId(productId)
                .description(description)
                .build();

        return productDescriptionRepository.save(newProductDescription);
    }
}
