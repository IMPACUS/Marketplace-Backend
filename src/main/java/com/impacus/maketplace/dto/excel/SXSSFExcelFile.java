package com.impacus.maketplace.dto.excel;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;

/**
 * 단일 시트 엑셀을 생성하는 클래스
 */
public class SXSSFExcelFile extends BaseSXSSFExcelFile {

    public SXSSFExcelFile(
            ExcelSheetData data
    ) {
        this(data, null);
    }

    public SXSSFExcelFile(
            ExcelSheetData data,
            @Nullable String password
    ) {
        ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
        this.password = password;
        generateExcelFile(data, metadata);
    }

    private void generateExcelFile(ExcelSheetData data, ExcelMetadata metadata) {
        renderHeaders(metadata);
        renderDataLines(data);
    }
}