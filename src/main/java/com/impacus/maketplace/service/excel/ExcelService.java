package com.impacus.maketplace.service.excel;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import com.impacus.maketplace.redis.repository.FileGenerationStatusRepository;
import com.impacus.maketplace.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelService {

    private final static String EXCEL_EXTENSION = "xlsx";
    private final S3Service s3Service;
    private final FileGenerationStatusRepository generationStatusRepository;
    private final ExcelProcessingService excelProcessingService;

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
    public FileGenerationStatusIdDTO generateExcel(List<?> data) throws InterruptedException {
        // 1. FileGenerationStatus 생성
        String fileGenerationStatusId = saveExcelGenerationStatus();

        // 2. 엑셀 생성
        excelProcessingService.createAndSaveExcel(fileGenerationStatusId, data);

        return FileGenerationStatusIdDTO.toDTO(fileGenerationStatusId);
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

    /**
     * 파일 생성 상태 조회 함수
     *
     * @param id
     * @return
     */
    public FileGenerationStatusDTO getFileGenerationStatus(String id) {
        try {
            FileGenerationStatus fileGenerationStatus = generationStatusRepository.getReferenceById(id);

            return FileGenerationStatusDTO.toDTO(fileGenerationStatus);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
