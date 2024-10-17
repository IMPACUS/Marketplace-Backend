package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.excel.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/excel")
public class ExcelController {
    private final ExcelService excelService;
    private final UserService userService;

    /**
     * [판매자, 관리자] 파일 생성 상태 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/status")
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    public ApiResponseEntity<FileGenerationStatusDTO> getFileGenerationStatus(
            @RequestParam(value = "id") String id
    ) {
        FileGenerationStatusDTO result = excelService.getFileGenerationStatus(id);

        return ApiResponseEntity.<FileGenerationStatusDTO>builder()
                .message("파일 생성 상태 조회 성공")
                .data(result)
                .build();
    }
}
