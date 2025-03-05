package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import com.impacus.maketplace.dto.point.CreateGreenLabelHistoryDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderGreenLabelPointHistory extends GreenLabelPointHistory {

    private String paymentId;

    public OrderGreenLabelPointHistory(
            CreateGreenLabelHistoryDTO dto
    ) {
        super(dto);
        this.paymentId = dto.getOrderId();
    }

    public static OrderGreenLabelPointHistory of(
            CreateGreenLabelHistoryDTO dto
    ) {
        return new OrderGreenLabelPointHistory(dto);
    }
}
