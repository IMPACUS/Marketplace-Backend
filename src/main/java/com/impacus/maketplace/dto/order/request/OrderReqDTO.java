package com.impacus.maketplace.dto.order.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderReqDTO {

    private Long userId;
    private Integer totalProductCost;
    private Integer deliveryFee;
    private Integer receivedPoint;
    private Integer discountAmount;
    private Integer usedPoint;


}
