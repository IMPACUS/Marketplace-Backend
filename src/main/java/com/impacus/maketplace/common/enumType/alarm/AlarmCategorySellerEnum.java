package com.impacus.maketplace.common.enumType.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmCategorySellerEnum {
    ORDER_DELIVERY("주문/배송"),
    WISH("찜"),
    INQUIRY_REVIEW("문의/리뷰"),
    ADVERTISEMENT("광고"),
    OPEN("입점 관련");
    private String value;
}
