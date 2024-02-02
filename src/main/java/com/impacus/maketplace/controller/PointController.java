package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.*;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.service.PointService;
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
    public ApiResponseEntity<PointMasterDto> addPoint(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PointRequestDto pointRequestDto) {
        pointRequestDto.setUserId(user.getId());
        PointMasterDto pointMasterDto = pointService.changePoint(pointRequestDto);

        return ApiResponseEntity.<PointMasterDto>builder()
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
    public ApiResponseEntity<List<PointHistoryDto>> getPointHistory(@AuthenticationPrincipal CustomUserDetails user) {
        PointHistorySearchDto pointHistorySearchDto = new PointHistorySearchDto();
        pointHistorySearchDto.setUserId(user.getId());

        List<PointHistoryDto> pointHistoryList = pointService.findPointHistory(pointHistorySearchDto);
        return ApiResponseEntity.<List<PointHistoryDto>>builder()
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
    public ApiResponseEntity<CurrentPointInfoDto> getCurrentMyPointStatus(@AuthenticationPrincipal CustomUserDetails user) {
        CurrentPointInfoDto data = pointService.findCurrentMyPointStatus(user);

        return ApiResponseEntity.<CurrentPointInfoDto>builder()
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
}
