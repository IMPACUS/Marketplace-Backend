package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

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
     * @param productRequest
     * @return
     */
    @PostMapping("/seller/new")
    public ApiResponseEntity<Object> addProduct(
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.addProduct(productImageList, productRequest, productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }

    /**
     * 등록된 상품을 삭제하는 API
     * TODO 다중 삭제 요청으로 변경 필요
     *
     * @param productId
     * @return
     */
    @DeleteMapping("/seller/{productId}")
    public ApiResponseEntity<Object> deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
        return ApiResponseEntity
                .builder()
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
    @PutMapping("/seller/{productId}")
    public ApiResponseEntity<Object> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.updateProduct(productId, productImageList, productRequest, productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }

    // 사용자 상품 조회
    @GetMapping("")
    public ApiResponseEntity<Object> getAllProductByNoAuth(
            @RequestParam(name = "category", required = false) SubCategory category,
            @PageableDefault(size = 16, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductDTO> productDTOList = productService.findProductByNoAuthAndCategory(category, pageable);
        return ApiResponseEntity
                .builder()
                .data(productDTOList)
                .build();
    }

    // 관리자/판매자 상품 조회
    @GetMapping("/seller")
    public ApiResponseEntity<Object> getAllProductByAuth(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "category", required = false) SubCategory category,
            @PageableDefault(size = 12, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductDTO> productDTOList = productService.findProductByAuthAndCategory(user.getId(), category, pageable);
        return ApiResponseEntity
                .builder()
                .data(productDTOList)
                .build();
    }

    // 사용자 단일 상품 조회

    // 관리자/판매자 단일 상품 조회
}
