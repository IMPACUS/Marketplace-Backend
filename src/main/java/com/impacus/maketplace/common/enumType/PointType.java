package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PointType {
    JOIN("00","회원가입"),
    SAVE("10","적립"),
    USE("20","결제 시 사용"),
    EXPIRE("30","소멸"),
    CHECK("40", "출석체크"),;

    private final String code;
    private final String value;

    public static PointType fromCode(String code) {
        return Arrays.stream(PointType.values()).filter(t -> t.getCode().equals(code)).findFirst()
                .orElseThrow();
    }
}
