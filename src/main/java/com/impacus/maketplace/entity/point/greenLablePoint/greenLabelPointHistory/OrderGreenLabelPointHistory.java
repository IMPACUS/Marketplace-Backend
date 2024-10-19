package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderGreenLabelPointHistory extends GreenLabelPointHistory {

    private Long orderId;
}
