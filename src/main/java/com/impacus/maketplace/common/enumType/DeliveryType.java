package com.impacus.maketplace.common.enumType;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryType {
    GENERAL_DELIVERY(1, "일반 배송"),
    PLUS_DELIVERY(2, "배송+");

    private final int code;
    private final String value;

    public static BooleanExpression containsEnumValue(EnumPath<DeliveryType> path, String keyword) {
        if (DeliveryType.GENERAL_DELIVERY.getValue().contains(keyword)) {
            return path.eq(DeliveryType.GENERAL_DELIVERY);
        } else if (DeliveryType.PLUS_DELIVERY.getValue().contains(keyword)) {
            return path.eq(DeliveryType.PLUS_DELIVERY);
        } else {
            return null;
        }
    }
}
