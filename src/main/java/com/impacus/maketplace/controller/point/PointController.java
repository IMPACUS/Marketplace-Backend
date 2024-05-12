package com.impacus.maketplace.controller.point;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.request.PointManageDTO;
import com.impacus.maketplace.dto.point.request.PointRequestDTO;
import com.impacus.maketplace.dto.point.response.CurrentPointInfoDTO;
import com.impacus.maketplace.dto.point.response.PointHistoryDTO;
import com.impacus.maketplace.dto.point.response.PointInfoDto;
import com.impacus.maketplace.dto.point.response.PointMasterDTO;
import com.impacus.maketplace.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/point")
public class PointController {

    private final PointService pointService;

    /**
     * 포인트를 사용하고, 적립하는 API
     *
     * @param pointRequestDto
     * @return
     */
    @PostMapping("/master/new")
    public ApiResponseEntity<PointMasterDTO> addPoint(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PointRequestDTO pointRequestDto) {
        pointRequestDto.setUserId(user.getId());
        PointMasterDTO pointMasterDto = pointService.changePoint(pointRequestDto);

        return ApiResponseEntity.<PointMasterDTO>builder()
                .data(pointMasterDto)
                .code(pointMasterDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .build();
    }

    /**
     *  나의 포인트 이력 API
     *
     * @param user
     * @return
     */
    @GetMapping("/history/list")
    public ApiResponseEntity<List<PointHistoryDTO>> getPointHistory(@AuthenticationPrincipal CustomUserDetails user) {
        PointHistorySearchDto pointHistorySearchDto = new PointHistorySearchDto();
        pointHistorySearchDto.setUserId(user.getId());

        List<PointHistoryDTO> pointHistoryList = pointService.findPointHistory(pointHistorySearchDto);
        return ApiResponseEntity.<List<PointHistoryDTO>>builder()
                .data(pointHistoryList)
                .build();

    }

    /**
     *  포인트정보 (단계, 현재 스코어, 예상 등급, xx 추가 적립시 등급상승) API
     *
     * @param user
     * @return
     */
    @GetMapping("/pointInfo")
    public ApiResponseEntity<PointInfoDto> getMyPointStatus(@AuthenticationPrincipal CustomUserDetails user) {
        PointInfoDto data = pointService.findMyPointInfo(user);

        return ApiResponseEntity.<PointInfoDto>builder()
                .data(data)
                .build();
    }

    @GetMapping("/myPointStatus")
    public ApiResponseEntity<CurrentPointInfoDTO> getCurrentMyPointStatus(@AuthenticationPrincipal CustomUserDetails user) {
        CurrentPointInfoDTO data = pointService.findCurrentMyPointStatus(user);

        return ApiResponseEntity.<CurrentPointInfoDTO>builder()
                .data(data)
                .build();
    }

    @GetMapping("/scheduledTest")
    public ApiResponseEntity<?> scheduledTest() {
        pointService.updateDisappearPoint();

        return ApiResponseEntity.<Object>builder().build();
    }


    /**
     *  - 소멸 예정 포인트 포인트 삭감 배치작업
     */


    /**
     * ADMIN 포인트 지급 및 수취
     */
    @PostMapping("/manage")
    public ApiResponseEntity<?> pointManageForAdmin(@Valid @RequestBody PointManageDTO pointManageDto) {
        boolean result = pointService.pointManage(pointManageDto);

        return ApiResponseEntity.<Object>builder().result(result).build();
    }
}
