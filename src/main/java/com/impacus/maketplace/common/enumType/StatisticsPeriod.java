package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum StatisticsPeriod {
    YESTERDAY("어제", new int[] {0, 5, 9, 13, 17, 21, 24}),
    LAST_7_DAYS("최근 7일간", new int[] {}),
    LAST_30_DAYS("최근 한 달간", new int[] {}),
    MONTHLY("월간", new int[] {}),
    YEARLY("연간", new int[] {});

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
