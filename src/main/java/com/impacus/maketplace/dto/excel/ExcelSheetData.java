package com.impacus.maketplace.dto.excel;

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
        return new ExcelSheetData(data, type);
    }
}
