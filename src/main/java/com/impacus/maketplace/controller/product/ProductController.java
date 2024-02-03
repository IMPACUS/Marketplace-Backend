package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
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


    /**
     * 새로운 상품을 등록하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param productRequest
     * @return
     */
    @PostMapping("/seller")
    public ApiResponseEntity<Object> addProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.addProduct(
                user.getId(),
                productImageList,
                productRequest,
                productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }

    /**
     * 상품 다중 삭제 API
     *
     * @param productIdList
     * @return
     */
    @DeleteMapping("/seller/")
    public ApiResponseEntity<Object> deleteAllProduct(@RequestParam(name = "productId") List<Long> productIdList) {
        productService.deleteAllProduct(productIdList);
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
        ProductDTO productDTO = productService.updateProduct(

                productId,
                productImageList,
                productRequest,
                productDescriptionImageList
        );
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }

    /**
     * 앱용 소비자가 전체 상품 조회 API
     *
     * @param category
     * @param pageable
     * @return
     */
    @GetMapping("")
    public ApiResponseEntity<Object> getAllProductForApp(
            @RequestParam(name = "category", required = false) SubCategory category,
            @PageableDefault(size = 15, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ProductDTO> productDTOList = productService.findProductByCategoryForApp(category, pageable);
        return ApiResponseEntity
                .builder()
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
    public ApiResponseEntity<Object> getAllProductForWeb(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "startAt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(name = "endAt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @PageableDefault(size = 12, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductForWebDTO> productDTOList = productService.findProductForWeb(user.getId(), startAt, endAt, pageable);
        return ApiResponseEntity
                .builder()
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
    public ApiResponseEntity<Object> getProductByNoAuth(@PathVariable(name = "productId") Long productId) {
        ProductDetailDTO productDetailDTO = productService.findProductDetail(productId);
        return ApiResponseEntity
                .builder()
                .data(productDetailDTO)
                .build();
    }

    // TODO 관리자/판매자 단일 상품 조회 - 아직 설계되지 않음.
}
