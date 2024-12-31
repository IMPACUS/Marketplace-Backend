package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorType implements ErrorType {
    ORDER_NOT_CONFIRMED_FOR_REVIEW("001_ORDER_NOT_CONFIRMED_FOR_REVIEW", "주문이 확정되지 않아 리뷰를 작성할 수 없습니다."),
    NOT_EXISTED_REVIEW_ID("002_NOT_EXISTED_REVIEW_ID", "존재하지 않는 리뷰 ID 입니다.");

    private final String code;
    private final String msg;
}
