package com.impacus.maketplace.dto.point.levelPoint;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import lombok.Getter;

@Getter
public class LevelPointDTO {
    private UserLevel userLevel;
    private Long levelPoint;
    private Long pointsRequiredForNextLevel;

    public LevelPointDTO(
            UserLevel userLevel,
            Long levelPoint
    ) {
        this.userLevel = userLevel;
        this.levelPoint = levelPoint;
        this.pointsRequiredForNextLevel = userLevel == UserLevel.ECO_VIP
                ? 0L
                : UserLevel.getUpgradeLevel(userLevel).getMinScore() - levelPoint;
    }
}
