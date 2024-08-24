package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.service.product.bundleDelivery.BundleDeliveryGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bundle-delivery-group")
public class BundleDeliveryGroupController {
    private final BundleDeliveryGroupService bundleDeliveryGroupService;

    /**
     * [관리자] 새로운 묶음 배송 그룹을 등록하는 API
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

    /**
     * [판매자] 묶음 배송 그룹을 수정하는 API
     *
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PutMapping("/{groupId}")
    public ApiResponseEntity<Void> updateBundleDeliveryGroup(
            @PathVariable(value = "groupId") Long groupId,
            @Valid @RequestBody CreateBundleDeliveryGroupDTO dto
    ) {
        bundleDeliveryGroupService.updateBundleDeliveryGroup(
                groupId,
                dto
        );
        return ApiResponseEntity
                .<Void>builder()
                .message("묶음 배송 그룹 수정 성공")
                .build();
    }
}
