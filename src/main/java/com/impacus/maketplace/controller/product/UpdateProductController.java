package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.UpdateProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class UpdateProductController {
    private final UpdateProductService updateProductService;

    /**
     * 등록된 상품을 수정하는 API
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
    @PutMapping("")
    public ApiResponseEntity<ProductDTO> updateProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart(value = "productImage", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "productDescriptionImage", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") UpdateProductDTO dto) {
        ProductDTO productDTO = updateProductService.updateProduct(
                user.getId(),
                productImageList,
                dto,
                productDescriptionImageList
        );
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }
}
