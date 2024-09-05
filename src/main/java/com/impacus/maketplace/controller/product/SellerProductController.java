package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.service.product.ReadProductService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/seller")
public class SellerProductController {
    private final ReadProductService productService;

    /**
     * [관리자] 상품 전체 조회 API
     *
     * @param user
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
        "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
        "or hasRole('ROLE_OWNER')")
    @GetMapping("/{sellerId}/products/details")
    public ApiResponseEntity<Page<ProductForWebDTO>> getProductsForWeb(
        @PathVariable Long sellerId,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
        @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
        @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductsForWeb(
            sellerId, keyword, startAt, endAt, pageable
        );
        return ApiResponseEntity
            .<Page<ProductForWebDTO>>builder()
            .data(productDTOList)
            .build();
    }
}
