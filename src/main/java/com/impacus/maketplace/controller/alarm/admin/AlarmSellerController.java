package com.impacus.maketplace.controller.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategorySellerEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategorySellerEnum;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.admin.*;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForSeller;
import com.impacus.maketplace.service.alarm.admin.AlarmSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/admin")
public class AlarmSellerController {
    private final AlarmSellerService alarmService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PostMapping("seller")
    public ApiResponseEntity<?> addAlarmSellerComment(@Valid @RequestBody AddAlarmSellerDto addAlarmDto) {
        AlarmCategorySellerEnum category = addAlarmDto.getCategory();
        Map<AlarmSubcategorySellerEnum, List<String>> subcategoryMap = addAlarmDto.getSubcategory();
        Set<AlarmSubcategorySellerEnum> subcategorySet = subcategoryMap.keySet();

        alarmService.inputValidation(category, subcategorySet);

        for (AlarmSubcategorySellerEnum subcategory : subcategorySet) {
            Optional<AlarmAdminForSeller> optional = alarmService.find(category, subcategory);
            if (optional.isEmpty()) alarmService.add(addAlarmDto, subcategory);
            else alarmService.update(optional.get(), subcategoryMap.get(subcategory));
        }

        return ApiResponseEntity.builder()
                .message("알림 문구가 성공적으로 저장됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @GetMapping("seller")
    public ApiResponseEntity<?> getAlarmSellerComment(@Valid @ModelAttribute GetAlarmSellerDto getAlarmDto) {
        AlarmCategorySellerEnum category = getAlarmDto.getCategory();
        Set<AlarmSubcategorySellerEnum> subcategorySet = getAlarmDto.getSubcategory();
        alarmService.inputValidation(category, subcategorySet);

        List<OutputAlarmSellerDto> output = new ArrayList<>();
        for (AlarmSubcategorySellerEnum subcategory : subcategorySet) {
            Optional<AlarmAdminForSeller> optional = alarmService.find(category, subcategory);
            optional.ifPresent(alarmAdminForSeller -> output.add(alarmAdminForSeller.toDto()));
        }

        return ApiResponseEntity.builder()
                .message("알림 문구가 성공적으로 조회됐습니다.")
                .data(output)
                .build();
    }
}
