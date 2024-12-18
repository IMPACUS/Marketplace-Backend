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
}
