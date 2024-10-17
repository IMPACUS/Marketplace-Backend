package com.impacus.maketplace.dto.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 멀티 시의 각 시트별로 그려질 바디 데이터를 담는 클래스
 */
public class ExcelSheetDataGroup {
    private final List<ExcelSheetData> data;

    private ExcelSheetDataGroup(List<ExcelSheetData> data) {
        this.data = new ArrayList<>(data);
    }

    public static ExcelSheetDataGroup of(ExcelSheetData... data) {
        List<ExcelSheetData> sheetData = (data == null) ? List.of() : List.of(data);
        return new ExcelSheetDataGroup(sheetData);
    }

    public List<ExcelSheetData> getExcelSheetData() {
        return Collections.unmodifiableList(data);
    }
}