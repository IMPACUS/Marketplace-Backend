package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AskType {
    ETC("ETC", "etc", "기타"),
    LEVEL("LEVEL", "level", "등급"),
    MEMBER("MEMBER", "member", "회원"),
    POINT("POINT", "point", "포인트"),
    REVIEW("REVIEW", "review", "리뷰"),
    SETTLE("SETTLE", "settle", "정산");

    private final String name;
    private final String code;
    private final String desc;
}
