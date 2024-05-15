package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface ProductCustomRepository {
    Page<ProductForWebDTO> findAllProduct(Long sellerId, LocalDate startAt, LocalDate endAt, Pageable pageable);

    DetailedProductDTO findProductByProductId(Long productId);

    boolean existsBySuperCategoryId(Long superCategoryId);

    Slice<ProductForAppDTO> findAllProductBySubCategoryId(Long userId, Long subCategoryId, Pageable pageable);
}
