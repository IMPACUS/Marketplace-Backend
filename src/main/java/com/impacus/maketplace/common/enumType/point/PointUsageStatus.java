package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointUsageStatus {
    UNUSED("01", "미사용"),
    IN_USE("02", "사용중"),
    COMPLETED("03", "사용 완료"),
    EXPIRED("04", "소멸");

    private final String code;
    private final String value;
}
