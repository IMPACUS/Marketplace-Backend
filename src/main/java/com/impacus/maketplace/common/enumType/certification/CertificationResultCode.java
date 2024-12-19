package com.impacus.maketplace.common.enumType.certification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CertificationResultCode {
    SUCCESS(1, "success"),
    FAIL(2, "failure");

    private final int code;
    private final String message;
}
