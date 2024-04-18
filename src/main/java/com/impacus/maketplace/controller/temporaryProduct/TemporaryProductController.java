package com.impacus.maketplace.controller.temporaryProduct;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.SimpleTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductDTO;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/temporary-product")
public class TemporaryProductController {

    private final TemporaryProductService temporaryProductService;

    /**
     * 임시 저장된 상품이 존재하는지 확인하는 API
     *
     * @param user
     * @return
     */
    @GetMapping("/exist")
    public ApiResponseEntity<Object> checkIsExistedTemporaryProduct(@AuthenticationPrincipal CustomUserDetails user) {
        IsExistedTemporaryProductDTO dto = temporaryProductService.checkIsExistedTemporaryProduct(user.getId());
        return ApiResponseEntity
                .builder()
                .data(dto)
                .build();
    }

    /**
     * 임시 저장 상품 데이터를 등록 혹은 수정하는 API
     *
     * @param productImageList
     * @param productDescriptionImageList
     * @param productRequest
     * @return
     */
    @PutMapping("/seller")
    public ApiResponseEntity<Object> addOrModifyTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart(value = "product-image", required = false) List<MultipartFile> productImageList,
            @RequestPart(value = "product-description-image", required = false) List<MultipartFile> productDescriptionImageList,
            @Valid @RequestPart(value = "product") ProductRequest productRequest) {
        SimpleTemporaryProductDTO simpleTemporaryProductDTO = temporaryProductService.addOrModifyTemporaryProduct(user.getId(), productImageList, productRequest, productDescriptionImageList);
        return ApiResponseEntity
                .builder()
                .data(simpleTemporaryProductDTO)
                .build();
    }

    @GetMapping("/seller")
    public ApiResponseEntity<Object> getTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user) {
        TemporaryProductDTO temporaryProductDTO = temporaryProductService.findTemporaryProduct(user.getId());
        return ApiResponseEntity
                .builder()
                .data(temporaryProductDTO)
                .build();
    }

}
