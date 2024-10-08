package com.impacus.maketplace.repository.point.greenLabelPoint.mapping;

import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotUsedGreenLabelPointAllocationDTO {
    private Long id;
    private Long remainPoint;
    private LocalDateTime expiredAt;
    private PointUsageStatus pointStatus;
}
