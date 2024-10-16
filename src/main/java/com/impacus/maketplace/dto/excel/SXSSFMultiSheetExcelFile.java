package com.impacus.maketplace.dto.excel;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;

/**
 * 멀티 시트 엑셀을 생성하는 클래스
 */
public class SXSSFMultiSheetExcelFile extends BaseSXSSFExcelFile {

    public SXSSFMultiSheetExcelFile(
            ExcelSheetDataGroup dataGroup
    ) {
        this(dataGroup, null);
    }

    public SXSSFMultiSheetExcelFile(
            ExcelSheetDataGroup dataGroup,
            @Nullable String password
    ) {
        exportExcelFile(dataGroup);
        this.password = password;
    }


    private void exportExcelFile(
            ExcelSheetDataGroup dataGroup
    ) {
        for (ExcelSheetData data : dataGroup.getExcelSheetData()) {
            ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
            renderHeaders(metadata);
            renderDataLines(data);
        }
    }
}