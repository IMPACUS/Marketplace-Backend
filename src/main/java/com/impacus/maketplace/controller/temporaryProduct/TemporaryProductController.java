package com.impacus.maketplace.controller.temporaryProduct;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.DetailStepProductDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductDTO;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductServiceImpl;
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

    private final TemporaryProductServiceImpl temporaryProductService;

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
     * step 1.
     * 기본 상품 데이터를 임시 저장 등록 혹은 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/basic")
    public ApiResponseEntity<Void> addOrModifyBasicTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody BasicStepProductDTO dto
    ) {
        temporaryProductService.addOrModifyTemporaryProductAtBasic(user.getId(), dto);
        return ApiResponseEntity
                .<Void>builder()
                .message("임시 상품 등록 혹은 수정 성공")
                .build();
    }

    /**
     * step 2.
     * 임시 저장 상품 옵션 데이터를 등록 혹은 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/options")
    public ApiResponseEntity<Void> addOrModifyTemporaryProductOption(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody OptionStepProductDTO dto
    ) {
        temporaryProductService.addOrModifyTemporaryProductAtOptions(user.getId(), dto);
        return ApiResponseEntity
                .<Void>builder()
                .message("임시 상품 등록 혹은 수정 성공")
                .build();
    }

    /**
     * step 3.
     * 임시 저장 상품 상세 데이터를 등록 혹은 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/details")
    public ApiResponseEntity<Void> addOrModifyTemporaryProductDetail(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody DetailStepProductDTO dto
    ) {
        temporaryProductService.addOrModifyTemporaryProductAtDetails(user.getId(), dto);
        return ApiResponseEntity
                .<Void>builder()
                .message("임시 상품 등록 혹은 수정 성공")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping()
    public ApiResponseEntity<TemporaryProductDTO> getTemporaryProduct(
            @AuthenticationPrincipal CustomUserDetails user) {
        TemporaryProductDTO dto = temporaryProductService.findTemporaryProduct(user.getId());
        return ApiResponseEntity
                .<TemporaryProductDTO>builder()
                .data(dto)
                .build();
    }

}