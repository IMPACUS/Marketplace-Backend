package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.bundleDelivery.request.CreateBundleDeliveryGroupDTO;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDetailDTO;
import com.impacus.maketplace.service.product.bundleDelivery.BundleDeliveryGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable(value = "groupId") Long groupId,
            @Valid @RequestBody CreateBundleDeliveryGroupDTO dto
    ) {
        bundleDeliveryGroupService.updateBundleDeliveryGroup(
                user.getId(),
                groupId,
                dto
        );
        return ApiResponseEntity
                .<Void>builder()
                .message("묶음 배송 그룹 수정 성공")
                .build();
    }

    /**
     * [판매자] 묶음 배송 그룹 조회
     *
     * @param groupId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @DeleteMapping("/{groupId}")
    public ApiResponseEntity<Void> deleteBundleDeliveryGroup(
            @PathVariable(value = "groupId") Long groupId
    ) {
        bundleDeliveryGroupService.deleteBundleDeliveryGroup(
                groupId
        );
        return ApiResponseEntity
                .<Void>builder()
                .message("묶음 배송 그룹 삭제 성공")
                .build();
    }

    /**
     * [판매자] 묶음 배송 그룹을 삭제하는 API
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping()
    public ApiResponseEntity<Page<BundleDeliveryGroupDetailDTO>> findDetailBundleDeliveryGroups(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort-by", required = false) String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @PageableDefault(sort = "groupId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // 사용자가 sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        Page<BundleDeliveryGroupDetailDTO> result = bundleDeliveryGroupService.findDetailBundleDeliveryGroups(
                user.getId(),
                keyword,
                pageable,
                sortBy,
                direction
        );
        return ApiResponseEntity
                .<Page<BundleDeliveryGroupDetailDTO>>builder()
                .message("묶음 배송 그룹 조회 성공")
                .data(result)
                .build();
    }
}
