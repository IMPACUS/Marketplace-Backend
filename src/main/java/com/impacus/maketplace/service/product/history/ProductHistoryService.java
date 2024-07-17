package com.impacus.maketplace.service.product.history;

import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.history.ProductHistory;
import com.impacus.maketplace.repository.product.history.ProductHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductHistoryService {
    private final ProductHistoryRepository productHistoryRepository;

    /**
     * ProductHistory 저장 함수
     *
     * @param product
     * @param productImages
     */
    @Transactional
    public void saveProductHistory(Product product, List<AttachFile> productImages) {
        ProductHistory productHistory = ProductHistory.toEntity(product, productImages);
        productHistoryRepository.save(productHistory);
    }
}
