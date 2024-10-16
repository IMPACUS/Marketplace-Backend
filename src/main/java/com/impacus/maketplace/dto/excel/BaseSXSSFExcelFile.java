package com.impacus.maketplace.dto.excel;


import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SuperClassReflectionUtils;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * password 엑셀 파일을 암호화할 비밀번호 (null이면 암호화하지 않음)
     */
    protected String password;

    public BaseSXSSFExcelFile() {
        this.workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
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

    /**
     * 생성된 엑셀 파일을 주어진 OutputStream에 저장하는 함수.
     *
     * @param stream stream 엑셀이 저장될 OutputStream
     * @throws IOException
     */
    @Override
    public void write(OutputStream stream) throws IOException {
        workbook.write(stream);
    }

    /**
     * 생성된 엑셀 파일을 주어진 OutputStream에 암호화하여 저장하는 함수.
     *
     * @param stream 엑셀이 저장될 OutputStream
     * @throws IOException
     */
    @Override
    public void writeWithEncryption(OutputStream stream) throws IOException {
        try {
            if (password == null) {
                write(stream);
            } else {
                POIFSFileSystem fileSystem = new POIFSFileSystem();
                OutputStream encryptorStream = getEncryptorStream(fileSystem, password);
                workbook.write(encryptorStream);
                encryptorStream.close();

                fileSystem.writeFilesystem(stream);
                fileSystem.close();
            }
        } finally {
            workbook.close();
            workbook.dispose();
            stream.close();
        }
    }

    public ByteArrayOutputStream writeWithEncryption() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writeWithEncryption(outputStream);

            return outputStream;
        }
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

    /**
     * 엑셀 저장 함수
     *
     * @param filePath 저장될 위치
     * @throws IOException
     */
    public void saveExcelInLocal(String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeWithEncryption(out);

            fileOut.write(out.toByteArray());
        }
    }

    /**
     * 엑셀을 response로 전달하는 함수
     *
     * @param response
     */
    public void sendResponse(HttpServletResponse response) throws IOException {
        writeWithEncryption(response.getOutputStream());
    }
}
