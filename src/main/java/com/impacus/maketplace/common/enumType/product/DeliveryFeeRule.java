package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryFeeRule {
    MINIMUM_FEE_IN_GROUP(1, "묶음 그룹에서 가장 작은 배송비로 부과"),
    MAXIMUM_FEE_IN_GROUP(2, "묶음 그룹에서 가장 큰 배송비로 부과");
    
    private final int code;
    private final String value;
}
