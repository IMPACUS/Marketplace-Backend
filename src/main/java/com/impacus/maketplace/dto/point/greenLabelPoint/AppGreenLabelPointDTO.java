package com.impacus.maketplace.dto.point.greenLabelPoint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppGreenLabelPointDTO {
    private Long greenLabelPoint;
    private Long pointsExpiringIn30Days;

    public static AppGreenLabelPointDTO toDTO(Long greenLabelPoint, Long pointsExpiringIn30Days) {
        return new AppGreenLabelPointDTO(greenLabelPoint, pointsExpiringIn30Days);
    }
}
