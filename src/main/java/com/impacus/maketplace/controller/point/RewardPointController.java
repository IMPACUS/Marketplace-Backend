package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import com.impacus.maketplace.service.point.RewardPointService;
import com.impacus.maketplace.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reward-point")
public class RewardPointController {
    private final RewardPointService rewardPointService;
    private final WishlistService pointService;

    /**
     * [관리자] 포인트 리워드 목록 조회 API
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Page<RewardPointDTO>> getRewardPoints(
            @PageableDefault(size = 6, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) RewardPointStatus status
    ) {
        Page<RewardPointDTO> result = rewardPointService.getRewardPoints(pageable, keyword, status);
        return ApiResponseEntity.<Page<RewardPointDTO>>builder()
                .message("포인트 리워드 목록 조회 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자] 포인트 리워드의 발급 상태 변경  API
     */
    @PatchMapping
    @PreAuthorize(" hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Void> updateRewardPointStatus(
            @RequestParam(value = "reward-point-ids") List<Long> rewardPointIds,
            @RequestParam(value = "status") RewardPointStatus status
    ) {
        rewardPointService.updateRewardPointStatus(rewardPointIds, status);
        return ApiResponseEntity.<Void>builder()
                .message("포인트 리워드의 발급 상태 변경 성공")
                .build();
    }


    /**
     * [관리자] 포인트 리워드의 발급 상태 변경  API
     */
    @GetMapping("test")
    public ApiResponseEntity<?> ddddd(
    ) {
        return ApiResponseEntity.builder()
                .data(pointService.findMarketNameByWishlistId(1L))
                .message("포인트 리워드의 발급 상태 변경 성공")
                .build();
    }
}
