package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;


    /**
     * 새로운 상품을 등록하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("")
    public ApiResponseEntity<ProductDTO> addProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") CreateProductDTO dto) {
        ProductDTO productDTO = productService.addProduct(
                user.getId(),
                productImageList,
                dto,
                productDescriptionImageList);
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }

    /**
     * 상품 다중 삭제 API
     *
     * @param productIdList
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteAllProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "product-id") List<Long> productIdList
    ) {
        productService.deleteAllProduct(user.getId(), productIdList);
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * 등록된 상품을 수정하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PutMapping("")
    public ApiResponseEntity<ProductDTO> updateProduct(
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") UpdateProductDTO dto) {
        ProductDTO productDTO = productService.updateProduct(
                productImageList,
                dto,
                productDescriptionImageList
        );
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }

    /**
     * 앱용 소비자가 전체 상품 조회 API
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
     * 웹용 판매자 상품 전체 조회 API
     * - 판매자의 브랜드가 등록한 상품들만 조회 가능
     * @param user
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping("/seller")
    public ApiResponseEntity<Page<ProductForWebDTO>> getAllProductBySellerIdForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductForWeb(
                user.getId(), UserType.ROLE_APPROVED_SELLER, keyword, startAt, endAt, pageable
        );
        return ApiResponseEntity
                .<Page<ProductForWebDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * 웹용 판매자 상품 전체 조회 API
     * - 판매자의 브랜드가 등록한 상품들만 조회 가능
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
    public ApiResponseEntity<Page<ProductForWebDTO>> getAllProductForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductForWeb(
                user.getId(), UserType.ROLE_ADMIN, keyword, startAt, endAt, pageable
        );
        return ApiResponseEntity
                .<Page<ProductForWebDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * 앱용 소비자가 단일 상품 조회 API
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
     * 판매자용 단일 상품 조회 API
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping("/product-details")
    public ApiResponseEntity<ProductDetailForWebDTO> getProductForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "product-id") Long productId
    ) {
        ProductDetailForWebDTO productDetailDTO = productService.findProductDetailForWeb(user.getId(), productId);
        return ApiResponseEntity
                .<ProductDetailForWebDTO>builder()
                .data(productDetailDTO)
                .build();
    }

    /**
     * 최근 본 상품 목록 조회 API
     *
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/recent-views")
    public ApiResponseEntity<Slice<ProductForAppDTO>> getProductForRecentViews(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15, direction = Sort.Direction.ASC, sort = "createAt") Pageable pageable) {
        Slice<ProductForAppDTO> productDTOList = productService.findProductForRecentViews(user.getId(), pageable);
        return ApiResponseEntity
                .<Slice<ProductForAppDTO>>builder()
                .data(productDTOList)
                .build();
    }

}