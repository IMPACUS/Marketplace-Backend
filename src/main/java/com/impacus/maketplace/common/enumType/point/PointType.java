package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    JOIN(0, "회원가입"),
    PURCHASE_PRODUCT(1, "상품 구매"),
    USE(2, "결제 시 사용"),
    EXPIRE(3, "소멸"),
    CHECK(4, "출석체크"),
    DORMANCY(5, "휴면에 의한 포인트 감소"),
    ADMIN_PROVIDE(6, "관리자에 의한 포인트 지급"),
    ADMIN_RECEIVE(7, "관리자에 의한 포인트 감소");


    private final int code;
    private final String value;
}
