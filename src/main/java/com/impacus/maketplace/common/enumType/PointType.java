package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {

    SAVE("10","적립"),
    USE("20","사용"),
    EXPIRE("30","소멸");

    private final String code;
    private final String value;
}
