package com.impacus.maketplace.service.excel;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.enumType.FileStatus;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.excel.ExcelSheetData;
import com.impacus.maketplace.dto.excel.SXSSFExcelFile;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import com.impacus.maketplace.redis.service.FileGenerationStatusService;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelProcessingService {
    private final FileGenerationStatusService fileGenerationStatusService;
    private final S3Service s3Service;

    /**
     * 엑셀 생성 및 저장 함수
     *
     * @param fileGenerationStatus
     * @param data                 excel 저장 데이터
     */
    @Async("fileGenerateExecutor")
    public void createAndSaveExcel(FileGenerationStatus fileGenerationStatus, List<?> data) {
        try {
            fileGenerationStatusService.updateFileGenerationStatus(fileGenerationStatus, FileStatus.IN_PROGRESS);

            ExcelSheetData sheet = ExcelSheetData.of(data, WebUserDTO.class);
            SXSSFExcelFile file = new SXSSFExcelFile(sheet);
            ByteArrayOutputStream outputStream = file.writeWithEncryption();
            URI uri = saveExcelInS3(outputStream);

            fileGenerationStatusService.updateFileGenerationStatus(fileGenerationStatus, FileStatus.COMPLETED, uri.toString());
        } catch (Exception e) {
            fileGenerationStatusService.updateFileGenerationStatus(fileGenerationStatus, FileStatus.FAILED);
            LogUtils.writeErrorLog("createAndSaveExcel", "File generation failed", e);
        }
    }

    /**
     * 엑셀 파일 명 반환 함수
     *
     * @return 엑셀명
     */
    private String getExcelFileName() {
        String fileName = com.impacus.maketplace.common.utils.StringUtils.generateUniqueNumber();
        return String.format("%s/%s.%s", DirectoryConstants.EXCEL_DIRECTORY, fileName, ExcelService.EXCEL_EXTENSION);
    }

    /**
     * s3에 엑셀 저장하는 함수
     *
     * @param out 엑셀 데이터
     * @return 엑셀 저장된 uri
     */
    private URI saveExcelInS3(ByteArrayOutputStream out) {
        return s3Service.putExcelInS3AndGetUrl(out.toByteArray(), getExcelFileName());
    }
}
