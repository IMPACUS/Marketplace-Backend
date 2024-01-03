package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.point.PointDto;
import com.impacus.maketplace.dto.point.PointRequestDto;
import com.impacus.maketplace.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponseEntity<PointDto> addPoint(@RequestBody PointRequestDto pointRequestDto) {

        return null;
    }

}
