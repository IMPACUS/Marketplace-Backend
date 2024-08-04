package com.impacus.maketplace.repository.point.greenLabelPoint.mapping;

import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotUsedGreenLabelPointAllocationDTO {
    private Long id;
    private Long remainPoint;
    private LocalDateTime expiredAt;
    private PointUsageStatus pointStatus;
}
