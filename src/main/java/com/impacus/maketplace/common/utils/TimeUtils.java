package com.impacus.maketplace.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public final class TimeUtils {

    /**
     * 시간을 반올림 하여 반환하는 함수
     * - 0~15 분 : 더 가까운 시간의 xx:00
     * - 16~45 분: xx:30
     * - 46~59 분: 더 가까운 시간의 xx:00
     *
     * @param time 시간
     * @return 반올림된 시간
     */
    public static LocalTime roundTimeToPolicy(LocalTime time) {
        int minute = time.getMinute();
        int hour = time.getHour();

        if (minute <= 15) {
            // 0~15분 -> 더 가까운 시간의 xx:00
            return LocalTime.of(hour, 0);
        } else if (minute <= 45) {
            // 16~45분 -> xx:30
            return LocalTime.of(hour, 30);
        } else {
            // 46~59분 -> 더 가까운 시간의 xx:00 (다음 시간으로 반올림)
            return LocalTime.of(hour, 0).plusHours(1);
        }
    }
}
