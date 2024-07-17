package com.impacus.maketplace.service.product.history;

import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.repository.product.history.ProductHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductHistoryService {
    private final ProductHistoryRepository productHistoryRepository;

    /**
     * ProductHistory 저장 함수
     *
     * @param productHistory
     */
    @Transactional
    public void saveProductHistory(ProductHistory productHistory) {
        productHistoryRepository.save(productHistory);
    }
}
