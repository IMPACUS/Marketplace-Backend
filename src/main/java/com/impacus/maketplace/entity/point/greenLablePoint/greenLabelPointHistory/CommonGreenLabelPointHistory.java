package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMMON")
public class CommonGreenLabelPointHistory extends GreenLabelPointHistory {
}
