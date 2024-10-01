package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import com.impacus.maketplace.service.point.RewardPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reward-point")
public class RewardPointController {
    private final RewardPointService rewardPointService;

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
}
