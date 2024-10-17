package com.impacus.maketplace.service.excel;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import com.impacus.maketplace.redis.repository.FileGenerationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelService {

    public static final String EXCEL_EXTENSION = "xlsx";

    private final FileGenerationStatusRepository generationStatusRepository;
    private final ExcelProcessingService excelProcessingService;

    /**
     * List 를 Excel로 생성하는 함수
     *
     * @param data excel 저장 데이터
     * @return
     */
    @Transactional
    public FileGenerationStatusIdDTO generateExcel(List<?> data) {
        // 1. FileGenerationStatus 생성
        FileGenerationStatus fileGenerationStatus = saveExcelGenerationStatus();
        String fileGenerationStatusId = fileGenerationStatus.getId();

        // 2. 엑셀 생성
        excelProcessingService.createAndSaveExcel(fileGenerationStatus, data);

        return FileGenerationStatusIdDTO.toDTO(fileGenerationStatusId);
    }


    /**
     * fileGenerationStatus 생성 함수
     *
     * @return fileGenerationStatus ID
     */
    @Transactional
    public FileGenerationStatus saveExcelGenerationStatus() {
        FileGenerationStatus fileGenerationStatus = FileGenerationStatus.toEntity(EXCEL_EXTENSION);
        generationStatusRepository.save(fileGenerationStatus);

        return fileGenerationStatus;
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
