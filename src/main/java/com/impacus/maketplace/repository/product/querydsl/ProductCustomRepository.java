package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface ProductCustomRepository {
    Page<ProductForWebDTO> findAllProduct(Long sellerId, String keyword, LocalDate startAt, LocalDate endAt, Pageable pageable);

    DetailedProductDTO findProductByProductId(Long userId, Long productId);

    boolean existsBySuperCategoryId(Long superCategoryId);

    Slice<ProductForAppDTO> findAllProductBySubCategoryId(Long userId, Long subCategoryId, Pageable pageable);

    Slice<ProductForAppDTO> findAllProductByProductIds(Long userId, List<Long> productIds, Pageable pageable);

    ProductDetailForWebDTO findProductDetailByProductId(Long sellerId, UserType userType, Long productId);
}
