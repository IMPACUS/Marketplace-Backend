package com.impacus.maketplace.service;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import com.impacus.maketplace.redis.repository.FileGenerationStatusRepository;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelService {

    private final static String EXCEL_EXTENSION = "xlsx";
    private final S3Service s3Service;
    private final FileGenerationStatusRepository generationStatusRepository;

    public String getExcelFileName() {
        String fileName = com.impacus.maketplace.common.utils.StringUtils.generateUniqueNumber();
        return String.format("%s/%s.%s", DirectoryConstants.EXCEL_DIRECTORY, fileName, EXCEL_EXTENSION);
    }

    public URI saveExcelInS3(ByteArrayOutputStream out) {
        return s3Service.putExcelInS3AndGetUrl(out.toByteArray(), getExcelFileName());
    }

    /**
     * List 를 Excel로 생성하는 함수
     *
     * @param data excel 저장 데이터
     * @return
     */
    @Transactional
    public FileGenerationStatusIdDTO generateExcel(List<?> data) {
        // 1. FileGenerationStatus 생성
        String fileGenerationStatusId = saveExcelGenerationStatus();

        // 2. 엑셀 생성
        createAndSaveExcel(fileGenerationStatusId, data);

        return FileGenerationStatusIdDTO.toDTO(fileGenerationStatusId);
    }

    /**
     * 엑셀 생성 및 저장 함수
     *
     * @param id   FileGenerationStatus id
     * @param data excel 저장 데이터
     */
    @Async("fileGenerateExecutor")
    @Transactional
    public void createAndSaveExcel(String id, List<?> data) {

        //        try {
        //ExcelSheetData ddd = ExcelSheetData.of(null, WebUserDTO.class);
        //SXSSFExcelFile file = new SXSSFExcelFile(ddd, "1111");
//            ByteArrayOutputStream outputStream = file.writeWithEncryption();
//
//            URI uri = excelService.saveExcelInS3(outputStream);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * fileGenerationStatus 생성 함수
     *
     * @return fileGenerationStatus ID
     */
    @Transactional
    public String saveExcelGenerationStatus() {
        FileGenerationStatus fileGenerationStatus = FileGenerationStatus.toEntity(EXCEL_EXTENSION);
        generationStatusRepository.save(fileGenerationStatus);

        return fileGenerationStatus.getId();
    }
}
