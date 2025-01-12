package com.impacus.maketplace.dto.excel;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface ExcelFile {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    void write(OutputStream stream) throws IOException;

    void writeWithEncryption(OutputStream stream) throws IOException;

    default <T> void createCell(Row row, int column, T value, CellStyle style) {
        if (value == null) return; // avoid NPE

        Cell cell = row.createCell(column);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(FORMATTER));
        } else if (value instanceof Enum<?>) {
            cell.setCellValue(((Enum<?>) value).toString());
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(FORMATTER));
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    default CellStyle createCellStyle(Workbook wb, boolean isBold) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(isBold);
        style.setFont(font);
        return style;
    }
}