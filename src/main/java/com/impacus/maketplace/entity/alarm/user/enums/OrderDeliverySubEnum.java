package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum OrderDeliverySubEnum {
    NONE("없음"),
    DELIVERY_START("배송시작"),
    DELIVERY_COMPLETE("배송완료");
    private String value;
}
