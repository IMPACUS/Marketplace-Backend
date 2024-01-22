package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.service.temporaryProduct.TemporaryProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
