package com.impacus.maketplace.common.enumType.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmSellerTimeEnum {
    All("24hr"),
    FIX("오전 9시~오후 10시"),
    CUSTOM("브랜드 영업 시간에 맞춤");
    private String value;
}
