package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDTO;
import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.service.product.ReadProductService;
import com.impacus.maketplace.service.product.bundleDelivery.BundleDeliveryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ReadProductController {

    private final ReadProductService productService;
    private final BundleDeliveryGroupService bundleDeliveryGroupService;

    /**
     * [앱] 전체 상품 조회 API
     *
     * @param subCategoryId
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<Slice<ProductForAppDTO>> getAllProductForApp(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "sub-category-id", required = false) Long subCategoryId,
            @PageableDefault(size = 15, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ProductForAppDTO> productDTOList = productService.findProductByCategoryForApp(user.getId(), subCategoryId, pageable);
        return ApiResponseEntity
                .<Slice<ProductForAppDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * [판매자] 상품 전체 조회 API
     * - 판매자의 브랜드가 등록한 상품들만 조회 가능
     * @param user
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping("/seller")
    public ApiResponseEntity<Page<ProductForWebDTO>> getProductsBySellerIdForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductsForWeb(
                user.getId(), UserType.ROLE_APPROVED_SELLER, keyword, startAt, endAt, pageable
        );
        return ApiResponseEntity
                .<Page<ProductForWebDTO>>builder()
                .data(productDTOList)
                .build();
    }

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
    @GetMapping("/admin")
    public ApiResponseEntity<Page<ProductForWebDTO>> getProductsForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductsForWeb(
                user.getId(), UserType.ROLE_ADMIN, keyword, startAt, endAt, pageable
        );
        return ApiResponseEntity
                .<Page<ProductForWebDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * [앱] 단일 상품 조회 API
     *
     * @param productId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/product-info")
    public ApiResponseEntity<DetailedProductDTO> getProductForApp(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "product-id") Long productId) {
        DetailedProductDTO detailedProductDTO = productService.findDetailedProduct(user.getId(), productId);
        return ApiResponseEntity
                .<DetailedProductDTO>builder()
                .data(detailedProductDTO)
                .build();
    }

    /**
     * [판매자/관리자] 단일 상품 조회 API
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping("/details/{productId}")
    public ApiResponseEntity<ProductDetailForWebDTO> getProductForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(name = "productId") Long productId
    ) {
        ProductDetailForWebDTO productDetailDTO = productService.findProductDetailForWeb(user.getId(), productId);
        return ApiResponseEntity
                .<ProductDetailForWebDTO>builder()
                .data(productDetailDTO)
                .build();
    }

    /**
     * [앱] 최근 본 상품 목록 조회 API
     *
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/recent-views")
    public ApiResponseEntity<Slice<ProductForAppDTO>> getProductForRecentViews(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15, direction = Sort.Direction.DESC, sort = "createAt") Pageable pageable) {
        Slice<ProductForAppDTO> productDTOList = productService.findProductForRecentViews(user.getId(), pageable);
        return ApiResponseEntity
                .<Slice<ProductForAppDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * [판매자/관리자] (상품 등록/수정용) 묶음 배송 그룹 목록 조회
     *
     * @param
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping("/bundle-delivery-groups")
    public ApiResponseEntity<Page<BundleDeliveryGroupDTO>> findBundleDeliveryGroupsBySeller(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(value = "seller-Id", required = false) Long sellerId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PageableDefault(size = 5, sort = "groupId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<BundleDeliveryGroupDTO> result = bundleDeliveryGroupService.findBundleDeliveryGroupsBySeller(
                user.getId(),
                sellerId,
                keyword,
                pageable
        );
        return ApiResponseEntity
                .<Page<BundleDeliveryGroupDTO>>builder()
                .message("(상품 등록/수정용) 묶음 배송 그룹 목록 조회")
                .data(result)
                .build();
    }

}