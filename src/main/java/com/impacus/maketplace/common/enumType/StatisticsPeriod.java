package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum StatisticsPeriod {
    YESTERDAY("어제", new int[] {0, 5, 9, 13, 17, 21, 24}), // 시간
    LAST_7_DAYS("최근 7일간", new int[] {1, 2,3,4,5,6,7}), // 일 전
    LAST_30_DAYS("최근 한 달간", new int[] {}), // 주차
    MONTHLY("월간", new int[] {1,2,3,4,5,6,7,8}), // 개월 전
    YEARLY("연간", new int[] {1, 2, 3,4,5,6,7}); // 년 전

    private final String description;
    private final int[] statisticsData;

    public int getPreviousStatisticsValue(int value) {
        if (statisticsData.length == 0) {
            return 0;
        }

        int index = List.of(statisticsData).indexOf(value);
        if (statisticsData.length == index+1) {
            return statisticsData[0];
        } else {
            return statisticsData[index+1];
        }
    }
}
