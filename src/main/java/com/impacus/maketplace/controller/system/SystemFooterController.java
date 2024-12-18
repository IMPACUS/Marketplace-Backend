package com.impacus.maketplace.controller.system;


import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.system.SystemFooterInputDto;
import com.impacus.maketplace.dto.system.SystemFooterOutputDto;
import com.impacus.maketplace.service.system.SystemFooterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/system/footer")
@RequiredArgsConstructor
public class SystemFooterController {
    private final SystemFooterService systemFooterService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PostMapping("")
    public ApiResponseEntity<?> systemFooterSave(@Valid SystemFooterInputDto inputDto) {
        systemFooterService.addAndUpdate(inputDto);

        return ApiResponseEntity.builder()
                .message("시스템 하단영역이 성공적으로 저장됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @GetMapping("")
    public ApiResponseEntity<?> systemFooterFind() {
        SystemFooterOutputDto systemFooterOutputDto = systemFooterService.find();

        return ApiResponseEntity.builder()
                .message("시스템 하단영역이 성공적으로 조회됐습니다.")
                .data(systemFooterOutputDto)
                .build();
    }
}
