package com.impacus.maketplace.dto.excel;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 엑셀을 그릴 때 필요한 메타 데이터를 보관하는 클래스
 */
@Getter
public class ExcelMetadata {
    private final Map<String, String> headerNames;
    private final List<String> fieldNames;
    private final String sheetName;

    public ExcelMetadata(
            Map<String, String> headerNames,
            List<String> fieldNames,
            String sheetName
    ) {
        this.headerNames = headerNames;
        this.fieldNames = fieldNames;
        this.sheetName = sheetName;
    }


    public String getHeaderName(String fieldName) {
        return headerNames.getOrDefault(fieldName, "");
    }
}
