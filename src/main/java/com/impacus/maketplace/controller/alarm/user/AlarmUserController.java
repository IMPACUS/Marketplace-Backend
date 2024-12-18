package com.impacus.maketplace.controller.alarm.user;


import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.GetUserAlarmDTO;
import com.impacus.maketplace.dto.alarm.user.SendUserPushDTO;
import com.impacus.maketplace.dto.alarm.user.UpdateUserAlarmDTO;
import com.impacus.maketplace.service.alarm.AlarmSendService;
import com.impacus.maketplace.service.alarm.user.AlarmUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/user")
public class AlarmUserController {
    private final AlarmUserService alarmUserService;
    private final AlarmSendService alarmSendService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("")
    public ApiResponseEntity<?> updateAlarmUser(@Valid @RequestBody UpdateUserAlarmDTO updateUserAlarmDto,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        alarmUserService.updateAlarm(updateUserAlarmDto, user.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 변경됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<?> getAlarmUser(@AuthenticationPrincipal CustomUserDetails user) {
        List<GetUserAlarmDTO> alarm = alarmUserService.findAlarm(user.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 조회됐습니다.")
                .data(alarm)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("push")
    public ApiResponseEntity<?> pushAlarmUser(@RequestBody SendUserPushDTO dto,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        alarmUserService.saveAndUpdateToken(dto.getToken(), user.getId());
        return ApiResponseEntity.builder()
                .message("푸시 토큰이 성공적으로 저장됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("push/test")
    public ApiResponseEntity<?> pushTestAlarmUser(@AuthenticationPrincipal CustomUserDetails user) {
        alarmSendService.testPush(user.getId());
        return ApiResponseEntity.builder()
                .message("푸시 전송됐습니다.")
                .build();
    }
}
