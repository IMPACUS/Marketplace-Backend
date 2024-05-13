package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.service.common.EnumService;
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
    private final EnumService enumService;


    /**
     * 새로운 상품을 등록하는 API (ROLE: seller)
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param productRequest
     * @return
     */
    @PostMapping("")
    public ApiResponseEntity<ProductDTO> addProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") CreateProductDTO productRequest) {
        ProductDTO productDTO = productService.addProduct(
                user.getId(),
                productImageList,
                productRequest,
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
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteAllProduct(@RequestParam(name = "product-id") List<Long> productIdList) {
        productService.deleteAllProduct(productIdList);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * 등록된 상품을 수정하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param productRequest
     * @return
     */
    @PutMapping("/{productId}")
    public ApiResponseEntity<ProductDTO> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") CreateProductDTO productRequest) {
        ProductDTO productDTO = productService.updateProduct(
                productId,
                productImageList,
                productRequest,
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
    @GetMapping("")
    public ApiResponseEntity<Slice<ProductDTO>> getAllProductForApp(
            @RequestParam(name = "sub-category-id", required = false) Long subCategoryId,
            @PageableDefault(size = 15, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ProductDTO> productDTOList = productService.findProductByCategoryForApp(subCategoryId, pageable);
        return ApiResponseEntity
                .<Slice<ProductDTO>>builder()
                .data(productDTOList)
                .build();
    }

    /**
     * 웹용 판매자/관리자 상품 전체 조회 API
     *
     * @param user
     * @param startAt
     * @param endAt
     * @param pageable
     * @return
     */
    @GetMapping("/seller")
    public ApiResponseEntity<Page<ProductForWebDTO>> getAllProductForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "start-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "end-at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(size = 12, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductForWeb(user.getId(), startAt, endAt, pageable);
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
    @GetMapping("{productId}")
    public ApiResponseEntity<DetailedProductDTO> getProductForApp(@PathVariable(name = "productId") Long productId) {
        DetailedProductDTO detailedProductDTO = productService.findDetailedProduct(productId);
        return ApiResponseEntity
                .<DetailedProductDTO>builder()
                .data(detailedProductDTO)
                .build();
    }

    /**
     * 판매자용 단일 상품 조회 API
     */
    @GetMapping("seller/{productId}")
    public ApiResponseEntity<ProductDetailForWebDTO> getProductForWeb(@AuthenticationPrincipal CustomUserDetails user, @PathVariable(name = "productId") Long productId) {
        ProductDetailForWebDTO productDetailDTO = productService.findProductDetailForWeb(user.getId(), productId);
        return ApiResponseEntity
                .<ProductDetailForWebDTO>builder()
                .data(productDetailDTO)
                .build();
    }

}