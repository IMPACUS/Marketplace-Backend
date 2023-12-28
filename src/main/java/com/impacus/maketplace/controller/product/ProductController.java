package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.addProduct(productImageList, productRequest, productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }

    /**
     * 등록된 상품을 삭제하는 API
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
            @RequestPart(value = "product") ProductRequest productRequest) {
        ProductDTO productDTO = productService.updateProduct(productId, productImageList, productRequest, productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(productDTO)
                .build();
    }
}
