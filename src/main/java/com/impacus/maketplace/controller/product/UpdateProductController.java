package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductImagesDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.UpdateProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class UpdateProductController {
    private final UpdateProductService updateProductService;

    /**
     * 이미지 업데이트 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/{productId}/product-images")
    public ApiResponseEntity<Void> updateProductImages(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(value = "productId") Long productId,
            @RequestBody UpdateProductImagesDTO dto
    ) {
        updateProductService.updateProductImages(
                user.getId(),
                productId,
                dto.getProductImages()
        );
        return ApiResponseEntity
                .<Void>builder()
                .message("상품 이미지 수정 성공")
                .build();
    }

    /**
     * 등록된 상품을 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/{productId}")
    public ApiResponseEntity<ProductDTO> updateProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateProductDTO dto
    ) {
        ProductDTO productDTO = updateProductService.updateProduct(
                user.getId(),
                productId,
                dto
        );
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }

    /**
     * 등록된 상품을 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/{productId}/overwrite")
    public ApiResponseEntity<ProductDTO> updateProductInForce(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateProductDTO dto
    ) {
        ProductDTO productDTO = updateProductService.updateProduct(
                user.getId(),
                productId,
                dto
        );
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }
}
