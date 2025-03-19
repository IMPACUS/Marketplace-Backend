package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticController {


    /**
     * 통계 그래프 API
     * @return
     */
    @GetMapping("/activities/chart")
    public ApiResponseEntity<?> getUserActivitiesInChart() {
        return null;
    }


    /**
     * 유저/판매자 활동 API
     * @return
     */
    @GetMapping("/activities/table")
    public ApiResponseEntity<?> getUserActivitiesInTable() {
        return null;
    }
}
