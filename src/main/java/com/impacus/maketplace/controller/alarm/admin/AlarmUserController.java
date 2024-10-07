package com.impacus.maketplace.controller.alarm.admin;

import com.impacus.maketplace.common.enumType.alarm.AlarmCategoryUserEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSubcategoryUserEnum;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.admin.*;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.service.alarm.admin.AlarmUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/admin")
public class AlarmUserController {
    private final AlarmUserService alarmService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PostMapping("user")
    public ApiResponseEntity<?> addAlarmUserComment(@Valid @RequestBody AddAlarmUserDto addAlarmDto) {
        AlarmCategoryUserEnum category = addAlarmDto.getCategory();
        Map<AlarmSubcategoryUserEnum, List<String>> subcategoryMap = addAlarmDto.getSubcategory();
        Set<AlarmSubcategoryUserEnum> subcategorySet = subcategoryMap.keySet();

        alarmService.inputValidation(category, subcategorySet);

        for (AlarmSubcategoryUserEnum subcategory : subcategorySet) {
            Optional<AlarmAdminForUser> optional = alarmService.find(category, subcategory);
            if (optional.isEmpty()) alarmService.add(addAlarmDto, subcategory);
            else alarmService.update(optional.get(), subcategoryMap.get(subcategory));
        }

        return ApiResponseEntity.builder()
                .message("알림 문구가 성공적으로 저장됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @GetMapping("user")
    public ApiResponseEntity<?> getAlarmUserComment(@Valid @ModelAttribute GetAlarmUserDto getAlarmDto) {
        AlarmCategoryUserEnum category = getAlarmDto.getCategory();
        Set<AlarmSubcategoryUserEnum> subcategorySet = getAlarmDto.getSubcategory();
        alarmService.inputValidation(category, subcategorySet);

        List<OutputAlarmUserDto> output = new ArrayList<>();
        for (AlarmSubcategoryUserEnum subcategory : subcategorySet) {
            Optional<AlarmAdminForUser> optional = alarmService.find(category, subcategory);
            optional.ifPresent(alarmAdminForUser -> output.add(alarmAdminForUser.toDto()));
        }

        return ApiResponseEntity.builder()
                .message("알림 문구가 성공적으로 조회됐습니다.")
                .data(output)
                .build();
    }


}
