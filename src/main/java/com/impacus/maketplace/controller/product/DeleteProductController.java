package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.service.product.DeleteProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class DeleteProductController {
    private final DeleteProductService deleteProductService;

    /**
     * 상품 다중 삭제 API
     *
     * @param productIdList
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteAllProduct(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "product-id") List<Long> productIdList
    ) {
        deleteProductService.deleteAllProduct(user.getId(), productIdList);
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }
}
