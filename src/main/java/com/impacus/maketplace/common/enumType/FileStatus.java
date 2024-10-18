package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileStatus {
    NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED
}
