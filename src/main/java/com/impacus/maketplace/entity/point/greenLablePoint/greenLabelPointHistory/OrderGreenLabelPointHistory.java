package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORDER")
public class OrderGreenLabelPointHistory extends GreenLabelPointHistory {

    private Long orderId;
}
