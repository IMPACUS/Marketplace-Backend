package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.dto.ProductTypeDTO;
import com.impacus.maketplace.dto.product.dto.SearchProductDTO;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.dto.product.response.AppProductDetailDTO;
import com.impacus.maketplace.dto.product.response.WebProductDetailDTO;
import com.impacus.maketplace.dto.product.response.WebProductTableDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface ProductCustomRepository {
    Page<WebProductTableDetailDTO> findProductDetailsForWeb(
            Long sellerId,
            String keyword,
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable
    );

    AppProductDetailDTO findProductByProductIdForApp(Long userId, Long productId);

    boolean existsBySuperCategoryId(Long superCategoryId);

    Slice<AppProductDTO> findAllProductBySubCategoryId(Long userId, Long subCategoryId, Pageable pageable);

    Slice<AppProductDTO> findProductsByProductIds(Long userId, List<Long> productIds, Pageable pageable);

    WebProductDetailDTO findProductDetailByProductId(Long sellerId, UserType userType, Long productId);

    /**
     * productIds의 상품들이 userId가 등록한 상품이 맞는지 확인하는 함수
     *
     * @param userId
     * @param productIds
     * @return
     */
    boolean checkIsSellerProductIds(Long userId, List<Long> productIds);

    Slice<AppProductDTO> findProductsByName(Long userId, String name, Pageable pageable);

    Slice<SearchProductDTO> findAllBy(Pageable pageable);

    List<ProductTypeDTO> findProductForPoint(List<Long> productIds);
}
