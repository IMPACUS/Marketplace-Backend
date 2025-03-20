package com.impacus.maketplace.service.statistic;

import com.impacus.maketplace.dto.statistics.response.AppStatisticDTO;
import com.impacus.maketplace.dto.statistics.response.UserSellerStatisticDTO;

import java.util.List;

public interface UserSellerStatisticService {
    int getYesterdayData();
    int getLast7DaysData();
    int getLast30DaysData();
    int getAverageOnMonthData();
}
