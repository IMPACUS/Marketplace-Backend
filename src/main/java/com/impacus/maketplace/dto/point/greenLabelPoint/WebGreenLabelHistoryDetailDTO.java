package com.impacus.maketplace.dto.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebGreenLabelHistoryDetailDTO {
    private Long historyId;          // 이력 id
    private long availablePoints;    // 보유 포인트
    private long levelPoint;       // 레벨 포인트
    private String pointDescription;        // 포인트 상태
    private long tradeAmount;
    private LocalDateTime createAt;  // 지급일자
    private Long usedPoint;

    @QueryProjection
    public WebGreenLabelHistoryDetailDTO(
            Long historyId,
            long greenLabelPoint,
            long levelPoint,
            PointType pointType,
            long tradeAmount,
            LocalDateTime createAt,
            PointStatus pointStatus
    ) {
        this.historyId = historyId;
        this.availablePoints = greenLabelPoint;
        this.levelPoint = levelPoint;
        this.pointDescription = pointType.getValue(); // TODO 주문 번호 추가하도록 수정
        this.createAt = createAt;
        this.tradeAmount = tradeAmount;
        if (pointStatus == PointStatus.USE || pointStatus == PointStatus.EXPIRE) {
            this.usedPoint = tradeAmount;
        }

    }
}
