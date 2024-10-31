package com.impacus.maketplace.common.enumType.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeStatus {
    RUN("진행 중"),
    STOP("중단 됨");
    private String value;
}
