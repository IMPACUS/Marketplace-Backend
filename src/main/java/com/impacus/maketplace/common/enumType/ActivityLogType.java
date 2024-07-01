package com.impacus.maketplace.common.enumType;

public enum ActivityLogType {
    CATEGORY_VIEW("카테고리 조회"),
    CATEGORY_EDIT("카테고리 수정");

    private final String description;

    ActivityLogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
