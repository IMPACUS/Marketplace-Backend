package com.impacus.maketplace.dto.point.greenLabelPoint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GreenLabelPointDTO {
    private Long greenLabelPoint;
    private Long pointsExpiringIn30Days;

    public static GreenLabelPointDTO toDTO(Long greenLabelPoint, Long pointsExpiringIn30Days) {
        return new GreenLabelPointDTO(greenLabelPoint, pointsExpiringIn30Days);
    }
}
