package com.impacus.maketplace.common.enumType.user;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLevel {

    NONE("등급 없음", 0, 0, 0, 0L, 0),
    BRONZE("BRONZE", 1, 0, 100000, 0L, 1.0),
    ROOKIE("ROOKIE", 2, 100001, 300000, 10000L, 1.5),
    SILVER("SILVER", 3, 300001, 500000, 50000L, 2.0),
    GOLD("GOLD", 4, 500001, 900000, 90000L, 2.5),
    ECO_VIP("ECO_VIP", 5, 900001, Integer.MAX_VALUE, 50000L, 3.0);

    private final String value;
    private final int level;
    private final int minScore;
    private final int maxScore;
    private final Long celebrationPoint;
    private final double reserveRate;

    /**
     * userLevel 에서 상승 시, 변동되어야 하는 레벨을 반환하는 함수
     *
     * @return
     */
    public static UserLevel getUpgradeLevel(UserLevel userLevel) {
        return switch (userLevel) {
            case NONE -> UserLevel.BRONZE;
            case BRONZE -> UserLevel.ROOKIE;
            case ROOKIE -> UserLevel.SILVER;
            case SILVER -> UserLevel.GOLD;
            case GOLD -> UserLevel.ECO_VIP;
            default -> throw new CustomException(CommonErrorType.UNKNOWN, "등급 변동을 할 수 없는 레벨입니다.");
        };
    }

    /**
     * userLevel 에서 하락 시, 변동되어야 하는 레벨을 반환하는 함수
     *
     * @return
     */
    public static UserLevel getDowngradeLevel(UserLevel userLevel) {
        return switch (userLevel) {
            case BRONZE, ROOKIE -> UserLevel.BRONZE;
            case SILVER -> UserLevel.ROOKIE;
            case GOLD -> UserLevel.SILVER;
            case ECO_VIP -> UserLevel.GOLD;
            default -> throw new CustomException(CommonErrorType.UNKNOWN, "등급 변동을 할 수 없는 레벨입니다.");
        };
    }

    /**
     * point가 레벨을 올릴 수 있는 포인트인지 확인하는 함수
     *
     * @param point 확인할 포인트
     * @return
     */
    public boolean checkIsPossibleUpgrade(Long point) {
        return (point > getMaxScore());
    }

    /**
     * point가 레벨이 하락되어야 하는 포인트인지 확인하는 함수
     *
     * @param point 확인할 포인트
     * @return
     */
    public boolean checkIsPossibleDowngrade(Long point) {
        return (point < getMinScore());
    }
}
