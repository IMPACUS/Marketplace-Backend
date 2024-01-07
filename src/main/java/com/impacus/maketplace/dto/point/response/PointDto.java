package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import lombok.Builder;

@Builder
public record PointDto(String levelName, String nextLevelName, int scorePer) {

    public PointDto(int score) {
        this(UserLevel.fromScore(score, false).toString(),
             UserLevel.fromScore(score, true).toString(),
             UserLevel.getScorePer(score));
    }
}
