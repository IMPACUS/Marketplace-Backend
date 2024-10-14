package com.impacus.maketplace.dto.excel;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 단일 시트 엑셀을 생성하는 클래스
 */
public class SXSSFExcelFile extends BaseSXSSFExcelFile {

    public SXSSFExcelFile(
            ExcelSheetData data,
            HttpServletResponse response
    ) throws IOException {
        this(data, response, null);
    }

    public SXSSFExcelFile(
            ExcelSheetData data,
            HttpServletResponse response,
            @Nullable String password
    ) throws IOException {
        ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
        exportExcelFile(data, metadata, response.getOutputStream(), password);
    }

    public SXSSFExcelFile(
            ExcelSheetData data
    ) throws IOException {
        ExcelMetadata metadata = ExcelMetadataFactory.getInstance().createMetadata(data.getType());
        exportExcelFile(data, metadata);
    }

    private void exportExcelFile(
            ExcelSheetData data,
            ExcelMetadata metadata
    ) throws IOException {
        renderHeaders(metadata);
        renderDataLines(data);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeWithEncryption(out, null);
    }


    private void exportExcelFile(
            ExcelSheetData data,
            ExcelMetadata metadata,
            ServletOutputStream stream,
            String password
    ) throws IOException {
        renderHeaders(metadata);
        renderDataLines(data);
        writeWithEncryption(stream, password);
    }
}