package com.impacus.maketplace.controller.alarm.seller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.seller.GetSellerAlarmDTO;
import com.impacus.maketplace.dto.alarm.seller.UpdateSellerAlarmDTO;
import com.impacus.maketplace.service.alarm.seller.AlarmSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/seller")
public class AlarmSellerController {
    private final AlarmSellerService alarmSellerService;

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PutMapping("")
    public ApiResponseEntity<?> updateAlarmSeller(@Valid @RequestBody UpdateSellerAlarmDTO updateSellerAlarmDto,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        alarmSellerService.updateAlarm(updateSellerAlarmDto, user.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 변경됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @GetMapping("")
    public ApiResponseEntity<?> getAlarmSeller(@AuthenticationPrincipal CustomUserDetails user) {
        List<GetSellerAlarmDTO> alarm = alarmSellerService.findAlarm(user.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 조회됐습니다.")
                .data(alarm)
                .build();
    }
}
