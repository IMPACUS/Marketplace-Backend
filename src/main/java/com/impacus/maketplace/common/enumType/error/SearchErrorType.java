package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchErrorType implements ErrorType {
    NOT_ID("001_NOT_ID", "해당 id는 존재하지 않습니다.");

    private String code;
    private String msg;
}
