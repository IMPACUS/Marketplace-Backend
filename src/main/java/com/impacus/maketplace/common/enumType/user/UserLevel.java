package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum UserLevel {

    LEVEL0(0,0,100),
    LEVEL1(1,101,200),
    LEVEL2(2,201,300);


    private final int level;
    private final int minScore;
    private final int maxScore;

    public static UserLevel fromLevel(int level) {
        if (level >= 1 && level <= 100) {
            return UserLevel.values()[level - 1];
        } else {
            return UserLevel.LEVEL0;
        }
    }

    public static UserLevel fromScore(int score) {
        return Arrays.stream(UserLevel.values())
                .sorted(Comparator.reverseOrder())
                .filter(level -> score >= level.getMinScore())
                .findFirst()
                .orElse(UserLevel.LEVEL0);
    }

    public static int getScorePer(int score) {
        UserLevel userLevel = fromScore(score);
        int scoreRange = userLevel.getMaxScore() - userLevel.getMinScore();
        double percentage = 100 - ((double) (userLevel.getMaxScore() - score) / scoreRange) * 100.0;
        int result = (int) Math.round(percentage);
        return result;
    }

}
