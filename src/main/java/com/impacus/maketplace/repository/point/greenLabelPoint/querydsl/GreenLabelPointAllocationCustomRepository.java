package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GreenLabelPointAllocationCustomRepository {
    List<NotUsedGreenLabelPointAllocationDTO> findNotUsedGreenLabelPointByUserId(@Param("userId") Long userId);

    void updateGreenLabelPointAllocationById(
            Long id,
            PointUsageStatus pointUsageStatus,
            Long remainPoint
    );
}
