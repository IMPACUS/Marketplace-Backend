package com.impacus.maketplace.dto.excel;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 단일 시트의 바디 데이터 정보를 담는 클래스
 */
@Getter
@AllArgsConstructor
public class ExcelSheetData {
    private final List<?> data;
    private final Class<?> type;

    public static ExcelSheetData of(List<?> data, Class<?> type) {
        if (data == null) {
            throw new CustomException(CommonErrorType.FAIL_TO_CREATE_EXCEL, "Cannot create null data for excel");
        }

        return new ExcelSheetData(data, type);
    }
}
