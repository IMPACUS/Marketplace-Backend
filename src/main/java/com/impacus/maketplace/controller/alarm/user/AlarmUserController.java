package com.impacus.maketplace.controller.alarm.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.UpdateUserAlarmDto;
import com.impacus.maketplace.service.alarm.user.AlarmUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/user")
public class AlarmUserController {
    private final AlarmUserService alarmUserService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("")
    public ApiResponseEntity<?> updateAlarmUser(@Valid @RequestBody UpdateUserAlarmDto updateUserAlarmDto,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        alarmUserService.updateAlarm(updateUserAlarmDto, user.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 변경됐습니다.")
                .build();
    }

}
