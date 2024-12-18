package com.impacus.maketplace.dto.point;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGreenLabelHistoryDTO {
    private Long userId;
    private PointType pointType;
    private PointStatus pointStatus;
    private Long tradeAmount;
    private Long unappliedPoint;
    private long levelPoint;
    private long greenLabelPoint;
    private String orderId;

    public static CreateGreenLabelHistoryDTO of(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint
    ) {
        return new CreateGreenLabelHistoryDTO(
                userId, pointType, pointStatus, tradeAmount, unappliedPoint, greenLabelPoint, levelPoint, null
        );
    }

    public static CreateGreenLabelHistoryDTO of(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint,
            String orderId
    ) {
        return new CreateGreenLabelHistoryDTO(
                userId, pointType, pointStatus, tradeAmount, unappliedPoint, greenLabelPoint, levelPoint, orderId
        );
    }
}
