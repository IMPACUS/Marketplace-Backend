package com.impacus.maketplace.dto.excel;


import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SuperClassReflectionUtils;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.List;

public abstract class BaseSXSSFExcelFile implements ExcelFile {

    protected static final int ROW_ACCESS_WINDOW_SIZE = 1000;
    protected static final int ROW_START_INDEX = 0;
    protected static final int COLUMN_START_INDEX = 0;

    protected SXSSFWorkbook workbook;
    protected Sheet sheet;

    public BaseSXSSFExcelFile() {
        this.workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
    }

    public static void saveExcel(String filePath, byte[] excelData) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            fileOut.write(excelData);
        }
    }

    protected void renderHeaders(ExcelMetadata excelMetadata) {
        sheet = workbook.createSheet(excelMetadata.getSheetName());
        Row row = sheet.createRow(ROW_START_INDEX);
        int columnIndex = COLUMN_START_INDEX;
        CellStyle style = createCellStyle(workbook, true);

        for (String fieldName : excelMetadata.getFieldNames()) {
            createCell(row, columnIndex++, excelMetadata.getHeaderName(fieldName), style);
        }
    }

    protected void renderDataLines(ExcelSheetData data) {
        CellStyle style = createCellStyle(workbook, false);
        int rowIndex = ROW_START_INDEX + 1;
        List<Field> fields = SuperClassReflectionUtils.getAllFields(data.getType());

        for (Object record : data.getData()) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = COLUMN_START_INDEX;
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelColumn.class)) {
                        field.setAccessible(true);
                        createCell(row, columnIndex++, field.get(record), style);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new CustomException(CommonErrorType.FAIL_TO_CREATE_EXCEL, "Error accessing data field rendering data lines.");
            }
        }
    }

    @Override
    public void write(OutputStream stream) throws IOException {
        workbook.write(stream);
    }

    @Override
    public void writeWithEncryption(OutputStream stream, String password) throws IOException {
        if (password == null) {
            write(stream);
        } else {
//            POIFSFileSystem fileSystem = new POIFSFileSystem();
//            OutputStream encryptorStream = getEncryptorStream(fileSystem, password);
//            workbook.write(encryptorStream);
//            encryptorStream.close();
//
//            fileSystem.writeFilesystem(stream);
//            fileSystem.close();
        }
        saveExcel("base-countries.xlsx", ((ByteArrayOutputStream) stream).toByteArray());

        workbook.close();
        workbook.dispose();
        stream.close();
    }

    private OutputStream getEncryptorStream(POIFSFileSystem fileSystem, String password) {
        try {
            Encryptor encryptor = new EncryptionInfo(EncryptionMode.agile).getEncryptor();
            encryptor.confirmPassword(password);
            return encryptor.getDataStream(fileSystem);
        } catch (IOException | GeneralSecurityException e) {
            throw new CustomException(CommonErrorType.FAIL_TO_CREATE_EXCEL,
                    "Failed to obtain encrypted data stream from POIFSFileSystem.");
        }
    }
}
