package com.impacus.maketplace.dto.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GreenLabelHistoryDTO {
    private Long historyId;
    private Long tradeAmount;
    private PointType pointType;
    private LocalDateTime expiredAt;
    private LocalDateTime createAt;


    public GreenLabelHistoryDTO(
            Long historyId,
            Long tradeAmount,
            PointType pointType,
            PointStatus pointStatus,
            LocalDateTime expiredAt,
            LocalDateTime createAt
    ) {
        this.historyId = historyId;
        this.tradeAmount = tradeAmount;
        this.pointType = pointType;
        this.createAt = createAt;

        setExpiredAt(pointStatus, expiredAt);
    }

    private void setExpiredAt(PointStatus pointStatus, LocalDateTime expiredAt) {
        if (pointStatus != PointStatus.GRANT || expiredAt.isBefore(LocalDateTime.now())) {
            this.expiredAt = null;
        } else {
            this.expiredAt = expiredAt;
        }
    }
}
