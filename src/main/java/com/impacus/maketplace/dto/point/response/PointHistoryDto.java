package com.impacus.maketplace.dto.point.response;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.entity.point.PointHistory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PointHistoryDto(PointType pointType, Integer changePoint, Boolean isManual, LocalDateTime createAt, LocalDateTime expiredAt) {

    public PointHistoryDto(PointHistory pointHistory) {
        this(pointHistory.getPointType(), pointHistory.getChangePoint(), pointHistory.getIsManual(), pointHistory.getCreateAt(), pointHistory.getExpiredAt());
    }

    @QueryProjection
    public PointHistoryDto(PointType pointType, Integer changePoint, Boolean isManual, LocalDateTime createAt, LocalDateTime expiredAt) {
        this.pointType = pointType;
        this.changePoint = changePoint;
        this.isManual = isManual;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
    }
}
