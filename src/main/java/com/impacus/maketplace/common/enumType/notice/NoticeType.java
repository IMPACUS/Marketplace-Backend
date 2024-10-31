package com.impacus.maketplace.common.enumType.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType {
    NOTICE("공지사항"),
    EVENT("이벤트");
    private String value;
}
