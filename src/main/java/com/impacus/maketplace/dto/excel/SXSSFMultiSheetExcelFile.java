package com.impacus.maketplace.dto.excel;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 멀티 시트 엑셀을 생성하는 클래스
 */
public class SXSSFMultiSheetExcelFile extends BaseSXSSFExcelFile {

    public SXSSFMultiSheetExcelFile(
            ExcelSheetDataGroup dataGroup,
            HttpServletResponse response
    ) throws IOException {
        this(dataGroup, response, null);
    }

    public SXSSFMultiSheetExcelFile(
            ExcelSheetDataGroup dataGroup,
            HttpServletResponse response,
            @Nullable String password
    ) throws IOException {
        exportExcelFile(dataGroup, response.getOutputStream(), password);
    }


    private void exportExcelFile(
            ExcelSheetDataGroup dataGroup,
            ServletOutputStream stream,
            String password
    ) throws IOException {
        for (ExcelSheetData data : dataGroup.getExcelSheetData()) {
            ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
            renderHeaders(metadata);
            renderDataLines(data);
        }
        writeWithEncryption(stream, password);
    }
}