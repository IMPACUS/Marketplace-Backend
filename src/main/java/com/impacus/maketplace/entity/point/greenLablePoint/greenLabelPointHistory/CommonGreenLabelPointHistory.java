package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMMON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonGreenLabelPointHistory extends GreenLabelPointHistory {

    public CommonGreenLabelPointHistory(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint
    ) {
        super(userId, pointType, pointStatus, tradeAmount, unappliedPoint, greenLabelPoint, levelPoint);
    }

    public static CommonGreenLabelPointHistory of(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint
    ) {
        return new CommonGreenLabelPointHistory(
                userId, pointType, pointStatus, tradeAmount, unappliedPoint, greenLabelPoint, levelPoint
        );
    }
}
