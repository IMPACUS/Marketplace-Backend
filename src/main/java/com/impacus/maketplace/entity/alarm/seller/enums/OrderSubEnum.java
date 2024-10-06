package com.impacus.maketplace.entity.alarm.seller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderSubEnum {
    NONE("없음"),
    DELIVERY_START("배송시작"),
    DELIVERY_COMPLETE("배송완료");
    private String value;
}
