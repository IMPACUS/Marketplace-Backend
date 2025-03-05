package com.impacus.maketplace.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public final class DateUtils {

    /**
     * refresh_token_expires_in 값을 LocalDate로 변환합니다.
     *
     * @param refreshTokenExpiresIn Integer 형식의 만료일 (일 단위)
     * @return 만료일을 나타내는 LocalDate
     */
    public static LocalDate convertIntegerToLocalDate(Integer refreshTokenExpiresIn) {
        if (refreshTokenExpiresIn == null || refreshTokenExpiresIn < 0) {
            throw new IllegalArgumentException("refresh_token_expires_in 값은 0 이상의 정수여야 합니다.");
        }
        return LocalDate.now().plusDays(refreshTokenExpiresIn);
    }

    /**
     * 현재 조회 시점 기준, 이번 달의 첫 번째 날짜를 반환합니다.
     *
     * @return 이번 달의 첫 번째 날짜 (예: 2025-02-01)
     */
    public static LocalDate getFirstDayOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * 오늘 기준으로 지정한 일수만큼 이전의 날짜를 반환합니다.
     *
     * @param daysAgo 오늘로부터 몇 일 전인지를 나타내는 정수
     * @return 오늘로부터 {@code daysAgo}일 전의 날짜 (예: daysAgo가 3이면 3일 전 날짜)
     */
    public static LocalDate getDateNDaysAgo(int daysAgo) {
        return LocalDate.now().minusDays(daysAgo);
    }

    /**
     * 이벤트 주차 기준으로, 오늘로부터 6일 전의 날짜를 반환합니다.
     * (예를 들어, 이벤트 주차를 계산할 때 기준 날짜로 사용)
     *
     * @return 오늘로부터 6일 전의 날짜 (이벤트 주차 기준 날짜)
     */
    public static LocalDate getEventWeekReferenceDate() {
        return LocalDate.now().minusDays(6);
    }

}
