package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.product.response.ProductDetailDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ProductCustomRepository {
    Page<ProductForWebDTO> findAllProduct(LocalDate startAt, LocalDate endAt, Pageable pageable);

    ProductDetailDTO findProductByProductId(Long productId);
}
