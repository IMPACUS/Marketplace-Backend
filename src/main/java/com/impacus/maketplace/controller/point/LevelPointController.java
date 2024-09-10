package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.levelPoint.LevelPointDTO;
import com.impacus.maketplace.service.point.levelPoint.LevelPointMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/level-point")
public class LevelPointController {

    private final LevelPointMasterService levelPointMasterService;

    /**
     * 레벨 포인트 정보 조회 API
     *
     * @param user
     * @return
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<LevelPointDTO> getLevelPointInformation(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        LevelPointDTO dto = levelPointMasterService.getLevelPointInformation(user.getId());
        return ApiResponseEntity.<LevelPointDTO>builder()
                .message("레벨 포인트 조회 성공")
                .data(dto)
                .build();
    }
}
