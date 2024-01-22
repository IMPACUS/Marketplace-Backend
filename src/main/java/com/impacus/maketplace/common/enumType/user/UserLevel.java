package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum UserLevel {

    BRONZE(1,0,100000, 0, 1.0),
    ROOKIE(2,100001,300000, 10000, 1.5),
    SILVER(3,300001,500000, 30000, 2.0),
    GOLD(4,500001,900000, 90000, 2.5),
    ECO_VIP(5,900001,999999999, 50000, 3.5),
    UNKNOWN(0,0,0,0,0);


    private final int level;
    private final int minScore;
    private final int maxScore;
    private final int celebrationPoint;
    private final double reserveRate;

    public static UserLevel fromLevel(int level) {
        if (level >= 1 && level <= 100) {
            return UserLevel.values()[level - 1];
        } else {
            return UserLevel.UNKNOWN;
        }
    }

    public static UserLevel fromScore(int score) {
        return Arrays.stream(UserLevel.values())
                .filter(level -> level != UserLevel.UNKNOWN)
                .sorted(Comparator.reverseOrder())
                .filter(level -> score >= level.getMinScore())
                .findFirst()
                .orElse(UserLevel.UNKNOWN);
    }

    public static UserLevel fromScore(int score, boolean next) {
        UserLevel userLevel = Arrays.stream(UserLevel.values())
                .filter(level -> level != UserLevel.UNKNOWN)
                .sorted(Comparator.reverseOrder())
                .filter(level -> score >= level.getMinScore())
                .findFirst()
                .orElse(UserLevel.UNKNOWN);

        if (userLevel == UserLevel.ECO_VIP) {
            return UserLevel.ECO_VIP;
        }
        if (next) {
            int level = userLevel.getLevel();
            return UserLevel.values()[level];
        }
        return userLevel;
    }

    public static int getScorePer(int score) {
        UserLevel userLevel = fromScore(score);

        if (userLevel == UserLevel.ECO_VIP) {
            return 100;
        }

        int scoreRange = userLevel.getMaxScore() - userLevel.getMinScore();
        double percentage = 100 - ((double) (userLevel.getMaxScore() - score) / scoreRange) * 100.0;
        int result = (int) Math.round(percentage);
        return result;
    }

    public static int getUpcomingPoint(int score) {
        UserLevel userLevel = fromScore(score);

        if (userLevel == UserLevel.ECO_VIP) {
            return 0;
        }
        return userLevel.getMaxScore() - score;
    }
}
