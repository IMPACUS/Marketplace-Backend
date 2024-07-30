package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointStatus {
    NONE("00", "알 수 없음"),
    GRANT("01", "포인트 지급"),
    USE("02", "포인트 사용"),
    RETURN("03", "포인트 반환"),
    EXPIRE("04", "포인트 소멸");

    private final String code;
    private final String value;
}
