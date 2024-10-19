package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.common.response.BaseInfoDTO;
import com.impacus.maketplace.service.common.BaseInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/base-info")
public class BaseInfoController {
    private final BaseInfoService baseInfoService;

    /**
     * [관리자] 포인트/쿠폰 기준 정보 조회 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @GetMapping("/reward")
    public ApiResponseEntity<BaseInfoDTO> findRewardBaseInfo() {
        BaseInfoDTO dto = baseInfoService.findRewardBaseInfo();
        return ApiResponseEntity.<BaseInfoDTO>builder()
                .message("포인트/쿠폰 기준 정보 조회 성공")
                .data(dto)
                .build();
    }

    /**
     * [관리자] 포인트/쿠폰 기준 정보 수정 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PutMapping("/reward")
    public ApiResponseEntity<Void> updateRewardBaseInfo(
            @Valid @RequestBody BaseInfoDTO dto
    ) {
        baseInfoService.updateRewardBaseInfo(dto);
        return ApiResponseEntity.<Void>builder()
                .message("포인트/쿠폰 기준 정보 수정 성공")
                .build();
    }
}
