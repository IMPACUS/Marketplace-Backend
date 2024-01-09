package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.PointDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.dto.product.request.WishlistRequest;
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
    public ApiResponseEntity<PointMasterDto> addPoint(@RequestBody PointRequestDto pointRequestDto) {

        PointMasterDto pointMasterDto = pointService.changePoint(pointRequestDto);

        return ApiResponseEntity.<PointMasterDto>builder()
                .data(pointMasterDto)
                .code(pointMasterDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .build();
    }


    @GetMapping("/history/list")
    public ApiResponseEntity<List<PointHistoryDto>> getPointHistory(@AuthenticationPrincipal CustomUserDetails user,
                                                                    @RequestBody(required = false) PointHistorySearchDto pointHistorySearchDto) {
        if (user == null) {
            throw new CustomException(ErrorType.NOT_EXISTED_EMAIL);
        }
        pointHistorySearchDto.setUserId(user.getId());

        List<PointHistoryDto> pointHistoryList = pointService.findPointHistory(pointHistorySearchDto);

        return ApiResponseEntity.<List<PointHistoryDto>>builder()
                .data(pointHistoryList)
                .build();
    }

}
