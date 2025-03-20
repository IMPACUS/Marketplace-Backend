package com.impacus.maketplace.service.statistic;

import com.impacus.maketplace.dto.statistics.response.AppStatisticDTO;

import java.util.List;

public interface AppStatisticService {
    List<AppStatisticDTO> getYesterdayData();
    List<AppStatisticDTO> getLast7DaysData();
    List<AppStatisticDTO> getLast30DaysData();
    List<AppStatisticDTO> getMonthlyData();
    List<AppStatisticDTO> getYearlyData();

}
