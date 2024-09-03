package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UpdateAlarmErrorType implements ErrorType {
    INVALID_ALARM_ID("001_INVALID_ALARM_ID", "존재하지 않는 데이터입니다."),
    INVALID_USER_ID("002_INVALID_USER_ID", "해당 계정에 존재하지 않는 데이터입니다.");

    private String code;
    private String msg;
}
