package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorType implements ErrorType {
    ORDER_NOT_CONFIRMED_FOR_REVIEW("001_ORDER_NOT_CONFIRMED_FOR_REVIEW", "주문이 확정되지 않아 리뷰를 작성할 수 없습니다."),
    NOT_EXISTED_REVIEW_ID("002_NOT_EXISTED_REVIEW_ID", "존재하지 않는 review ID 입니다."),
    EXISTED_REVIEW_REPLY("003_EXISTED_REVIEW_REPLY", "이미 답변이 등록된 리뷰입니다."),
    NOT_EXISTED_REVIEW_REPLY_ID("004_NOT_EXISTED_REVIEW_REPLY_ID", "존재하지 않는 리뷰 답변 ID 입니다.");


    private final String code;
    private final String msg;
}
