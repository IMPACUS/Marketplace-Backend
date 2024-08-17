package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDTO;
import com.impacus.maketplace.service.product.CreateProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
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
public class CreateProductController {
    private final CreateProductService createProductService;

    /**
     * 새로운 상품을 등록하는 API
     *
     * @param productImageList
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
            @Valid @RequestPart(value = "product") CreateProductDTO dto
    ) {
        ProductDTO productDTO = createProductService.addProduct(
                user.getId(),
                productImageList,
                dto
        );
        return ApiResponseEntity
                .<ProductDTO>builder()
                .data(productDTO)
                .build();
    }
}
