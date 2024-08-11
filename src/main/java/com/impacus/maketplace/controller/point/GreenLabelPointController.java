package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelPointDTO;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

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
    public ApiResponseEntity<Slice<GreenLabelHistoryDTO>> getGreenLabelPointHistories(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Slice<GreenLabelHistoryDTO> dtos = greenLabelPointHistoryService.getGreenLabelPointHistories(user.getId(), pageable);
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
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<GreenLabelPointDTO> getGreenLabelPointInformation(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        GreenLabelPointDTO dto = greenLabelPointAllocationService.getGreenLabelPointInformation(user.getId());
        return ApiResponseEntity.<GreenLabelPointDTO>builder()
                .message("그린 라벨 포인트 조회 성공")
                .data(dto)
                .build();
    }
}
