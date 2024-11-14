package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchErrorType implements ErrorType {
    NOT_ENTITY("001_NOT_ENTITY", "해당 엔터티는 존재하지 않습니다."),
    ENTITY_EXIST("002_NOT_EXIST", "해당 엔터티는 이미 존재합니다.");
    private String code;
    private String msg;
}
