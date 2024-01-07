package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.response.PointDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/point")
public class PointController {

    private final PointService pointService;

    @PostMapping("/save")
    public ApiResponseEntity<PointMasterDto> addPoint(@RequestBody PointRequestDto pointRequestDto) {

        ApiResponseEntity<PointMasterDto> result = new ApiResponseEntity<>();

        PointMasterDto pointMasterDto = pointService.changePoint(pointRequestDto);
        if (pointMasterDto != null) {
            result.setData(pointMasterDto);
        } else {
            result.setCode(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

}
