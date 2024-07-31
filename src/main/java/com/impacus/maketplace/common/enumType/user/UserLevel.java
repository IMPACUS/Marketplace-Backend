package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLevel {

    NONE("등급 없음", 0, 0, 0, 0, 0),
    BRONZE("BRONZE", 1,0,100000, 0, 1.0),
    ROOKIE("ROOKIE", 2,100001,300000, 10000, 1.5),
    SILVER("SILVER", 3, 300001, 500000, 50000, 2.0),
    GOLD("GOLD", 4,500001,900000, 90000, 2.5),
    ECO_VIP("ECO_VIP", 5, 900001, Integer.MAX_VALUE, 50000, 3.0);

    private final String value;
    private final int level;
    private final int minScore;
    private final int maxScore;
    private final int celebrationPoint;
    private final double reserveRate;

    /**
     * point가 레벨을 올릴 수 있는 포인트 인지 확인하는 함수
     *
     * @param point 확인할 포인트
     * @return
     */
    public boolean checkIsPossibleUpgrade(Long point) {
        return (point > getMaxScore());
    }
}
