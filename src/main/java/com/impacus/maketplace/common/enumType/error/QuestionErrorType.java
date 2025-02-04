package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorType implements ErrorType {

    NOT_EXISTED_QUESTION_ID("001_NOT_EXISTED_QUESTION_ID", "존재하지 않는 문의 ID 입니다."),
    EXISTED_QUESTION_REPLY("002_EXISTED_QUESTION_REPLY", "이미 답변이 등록된 문의입니다."),
    NOT_EXISTED_QUESTION_REPLY_ID("003_NOT_EXISTED_QUESTION_REPLY_ID", "존재하지 않는 문의 답변 ID 입니다."),
    EXISTED_QUESTION("004_EXISTED_QUESTION", "이미 문의가 존재하는 상품 주문입니다.");
    
    private final String code;
    private final String msg;
}
