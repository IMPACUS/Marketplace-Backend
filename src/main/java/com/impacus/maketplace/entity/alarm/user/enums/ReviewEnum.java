package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ReviewEnum {
    REVIEW("리뷰"),
    SERVICE_EVALUATION("서비스 평가");
    private String value;
}
