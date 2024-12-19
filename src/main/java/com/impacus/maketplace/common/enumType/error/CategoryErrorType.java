package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorType implements ErrorType {
    DUPLICATED_SUPER_CATEGORY("022_DUPLICATED_SUPER_CATEGORY", "중복된 1차 카테고리 명이 존재합나디."),
    NOT_EXISTED_SUPER_CATEGORY("023_NOT_EXISTED_SUPER_CATEGORY", "존재하지 않는 1차 카테고리 옵션입니다."),
    NOT_EXISTED_SUB_CATEGORY("205_NOT_EXISTED_SUB_CATEGORY", "존재하지 않는 2차 카테고리 옵션입니다."),
    EXCEED_MAX_SUB_CATEGORY("027_EXCEED_MAX_SUB_CATEGORY", "생성할 수 있는 최대 2차 카테고리 수를 초과하였습니다."),
    CANNOT_DELETE_SUB_CATEGORY_WITH_PRODUCT("028_CANNOT_DELETE_SUB_CATEGORY_WITH_PRODUCT", "상품이 존재하는 2차 카테고리는 삭제할 수 없습니다."),
    CANNOT_DELETE_SUPER_CATEGORY_WITH_PRODUCT("029_CANNOT_DELETE_SUPER_CATEGORY_WITH_PRODUCT", "상품이 존재하는 1차 카테고리는 삭제할 수 없습니다."),
    DUPLICATED_SUB_CATEGORY("050_DUPLICATED_SUB_CATEGORY", "중복된 2차 카테고리 명이 존재합나다.");

    private final String code;
    private final String msg;
}
