package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum OrderDeliveryEnum {
    COMPLETE("결제완료"),
    DELIVERY("배송"),
    CANCEL("반품/교환/주문취소");
    private String value;
}
