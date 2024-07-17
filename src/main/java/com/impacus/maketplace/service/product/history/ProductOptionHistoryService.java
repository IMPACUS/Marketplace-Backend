package com.impacus.maketplace.service.product.history;

import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.entity.product.history.ProductOptionHistory;
import com.impacus.maketplace.repository.product.history.ProductOptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionHistoryService {
    private final ProductOptionHistoryRepository productOptionHistoryRepository;

    /**
     * ProductOptionHistory 를 저장하는 함수
     *
     * @param productOptions
     */
    @Transactional
    public void saveAllProductOptionHistory(List<ProductOption> productOptions) {
        List<ProductOptionHistory> productOptionHistories = productOptions.stream()
                .map(ProductOptionHistory::toEntity)
                .toList();

        productOptionHistoryRepository.saveAll(productOptionHistories);
    }
}
