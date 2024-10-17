package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum AlarmErrorType implements ErrorType {
    NOT_MATCH_CATEGORY("001_NOT_MATCH_CATEGORY", "상단, 하단 카테고리가 일치하지 않습니다."),
    NOT_COMMENT_IN_SUBCATEGORY("002_NOT_COMMENT_IN_SUBCATEGORY", "데이터들이 subcategory에 다 들어있지 않습니다."),
    NO_EXIST_USER("003_NO_EXIST_USER", "존재하지 않는 사용자입니다."),
    NO_EXIST_SELLER("004_NO_EXIST_SELLER", "존재하지 않는 판매자입니다."),
    NO_EXIST_SELLER_IN_BRAND("005_NO_EXIST_SELLER_IN_BRAND", "브랜드에 존재하지 않는 판매자입니다.");

    private String code;
    private String msg;
}
