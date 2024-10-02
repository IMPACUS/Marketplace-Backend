package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.dto.point.greenLabelPoint.AppGreenLabelPointDTO;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GreenLabelPointAllocationCustomRepository {
    List<NotUsedGreenLabelPointAllocationDTO> findNotUsedGreenLabelPointByUserId(@Param("userId") Long userId);

    void updateGreenLabelPointAllocationById(
            Long id,
            PointUsageStatus pointUsageStatus,
            Long remainPoint
    );

    AppGreenLabelPointDTO findPointInformationByUserId(Long userId);

    /**
     * 사용자에게 해당 포인트 포인트 타입으로 지급된 가장 최근 지급 날짜 반환 함수
     *
     * @param userId
     * @param pointType
     * @return
     */
    LocalDateTime findRecentAllocatedPointAtByUserIdAndPointType(Long userId, PointType pointType);

    /**
     * 오늘 사용자에게 해당 포인트 포인트 타입으로 지급된 포인트의 수 반환 함수
     *
     * @param userId
     * @param pointType
     * @return
     */
    Long findAllocatedPointCntByUserIdAndPointType(Long userId, PointType pointType);
}
