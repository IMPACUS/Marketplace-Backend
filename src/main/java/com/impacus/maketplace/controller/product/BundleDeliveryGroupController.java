package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.service.product.bundleDelivery.BundleDeliveryGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bundle-delivery-group")
public class BundleDeliveryGroupController {
    private final BundleDeliveryGroupService bundleDeliveryGroupService;

    /**
     * [관리자, 판매자] 새로운 상품을 등록하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PostMapping()
    public ApiResponseEntity<Void> addBundleDeliveryGroup(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateBundleDeliveryGroupDTO dto
    ) {
        bundleDeliveryGroupService.addBundleDeliveryGroup(
                user.getId(),
                dto
        );
        return ApiResponseEntity
                .<Void>builder()
                .message("묶음 배송 그룹 생성 성공")
                .build();
    }
}
