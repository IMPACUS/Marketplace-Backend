package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {


    /**
     * 설계도 - 추후 API 설계 이후 정리 예정
     *
     * 1) /api/v1/admin/list
     *
     */


    // 여기는 api 통신하는지만 테스트용
    @GetMapping("/test")
    public ApiResponseEntity<?> test() {
        String result = "test";

        return ApiResponseEntity
                .builder()
                .data(result)
                .build();
    }
}
