package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.IssuePointDTO;
import com.impacus.maketplace.service.point.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/point")
public class PointController {
    private final PointService pointService;

    /**
     * [관리자] (모든 회원) 포인트 지급/수취 API
     */
    @PatchMapping("/issue/users")
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Void> issuePointForAllUsers(
            @Valid @RequestBody IssuePointDTO dto
    ) {
        pointService.issueUserRewardForAllUsers(dto);
        return ApiResponseEntity.<Void>builder()
                .message("[관리자] (모든 회원) 포인트 지급/수취 성공")
                .build();
    }

    /**
     * [관리자] (특정 회원) 포인트 지급/수취 API
     */
    @PatchMapping("/issue")
    @PreAuthorize("hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<Void> issuePointForUser(
            @Valid @RequestBody IssuePointDTO dto
    ) {
        pointService.issueUserRewardForUser(dto);
        return ApiResponseEntity.<Void>builder()
                .message("(특정 회원) 포인트 지급/수취 성공")
                .build();
    }
}
