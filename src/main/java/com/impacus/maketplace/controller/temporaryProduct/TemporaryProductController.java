package com.impacus.maketplace.controller.temporaryProduct;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.SimpleTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductDTO;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
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
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PutMapping("")
    public ApiResponseEntity<Object> addOrModifyTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CreateProductDTO dto) {
        SimpleTemporaryProductDTO simpleTemporaryProductDTO = temporaryProductService.addOrModifyTemporaryProduct(user.getId(), dto);
        return ApiResponseEntity
                .builder()
                .data(simpleTemporaryProductDTO)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping("")
    public ApiResponseEntity<TemporaryProductDTO> getTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user) {
        TemporaryProductDTO dto = temporaryProductService.findTemporaryProduct(user.getId());
        return ApiResponseEntity
                .<TemporaryProductDTO>builder()
                .data(dto)
                .build();
    }

}