package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.greenLabelPoint.AppGreenLabelPointDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDetailDTO;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/green-label-point")
public class GreenLabelPointController {
    private final GreenLabelPointHistoryService greenLabelPointHistoryService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;


    /**
     * 리워드 포인트 리스트 조회 API
     *
     * @param user
     * @param pageable
     * @return
     */
    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<Slice<GreenLabelHistoryDTO>> getGreenLabelPointHistoriesForApp(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15, sort = "createAt", direction = Direction.ASC) Pageable pageable
    ) {
        Slice<GreenLabelHistoryDTO> dtos = greenLabelPointHistoryService.getGreenLabelPointHistoriesForApp(user.getId(), pageable);
        return ApiResponseEntity.<Slice<GreenLabelHistoryDTO>>builder()
                .message("리워드 포인트 목록 조회 성공")
                .data(dtos)
                .build();
    }

    /**
     * 그린 라벨 포인트 정보 조회 API
     *
     * @param user
     * @return
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<AppGreenLabelPointDTO> getGreenLabelPointInformation(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        AppGreenLabelPointDTO dto = greenLabelPointAllocationService.getGreenLabelPointInformation(user.getId());
        return ApiResponseEntity.<AppGreenLabelPointDTO>builder()
                .message("그린 라벨 포인트 조회 성공")
                .data(dto)
                .build();
    }

    /**
     * [관리자] 포인트 지급 목록 조회 API
     */
    @GetMapping("/allocation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Page<WebGreenLabelHistoryDTO>> getGreenLabelPointHistoriesForWeb(
            @PageableDefault(size = 6, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) RewardPointStatus status,
            @RequestParam(value = "start-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(value = "end-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt
    ) {
        Page<WebGreenLabelHistoryDTO> result = greenLabelPointHistoryService.getGreenLabelPointHistoriesForWeb(pageable, keyword, status, startAt, endAt);
        return ApiResponseEntity.<Page<WebGreenLabelHistoryDTO>>builder()
                .message("포인트 지급 목록 조회 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자] 회원님의 포인트 지급 내역 조회 API
     */
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Page<WebGreenLabelHistoryDetailDTO>> getGreenLabelPointHistoryDetailsForWeb(
            @PathVariable("userId") Long userId,
            @PageableDefault(size = 6, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<WebGreenLabelHistoryDetailDTO> result = greenLabelPointHistoryService.getGreenLabelPointHistoryDetailsForWeb(userId, pageable);
        return ApiResponseEntity.<Page<WebGreenLabelHistoryDetailDTO>>builder()
                .message("회원님의 포인트 지급 내역 조회 성공")
                .data(result)
                .build();
    }
}
