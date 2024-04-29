package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ProductCustomRepository {
    Page<ProductForWebDTO> findAllProduct(LocalDate startAt, LocalDate endAt, Pageable pageable);

    DetailedProductDTO findProductByProductId(Long productId);

    boolean existsBySuperCategoryId(Long superCategoryId);
}
