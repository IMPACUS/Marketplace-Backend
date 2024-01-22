package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import lombok.Builder;

@Builder
public record PointInfoDto(String currentLevel, Integer userScore, Integer minScore, Integer maxScore, Integer upcomingPoint, String nextLevel, Integer scorePer) {
    public PointInfoDto(int score) {
        this(UserLevel.fromScore(score).toString(),
             score,
             UserLevel.fromScore(score).getMinScore(),
             UserLevel.fromScore(score).getMaxScore(),
             UserLevel.getUpcomingPoint(score),
             UserLevel.fromScore(score, true).toString(),
             UserLevel.getScorePer(score));
    }
}
