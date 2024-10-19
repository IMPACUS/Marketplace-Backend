package com.impacus.maketplace.controller.alarm.seller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.seller.SendSellerTextDto;
import com.impacus.maketplace.dto.alarm.seller.UpdateSellerAlarmDto;
import com.impacus.maketplace.service.alarm.AlarmSendService;
import com.impacus.maketplace.service.alarm.seller.AlarmSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/seller")
public class AlarmSellerController {
    private final AlarmSellerService alarmSellerService;

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PutMapping("")
    public ApiResponseEntity<?> updateAlarmSeller(@Valid @RequestBody UpdateSellerAlarmDto updateSellerAlarmDto,
                                                  @AuthenticationPrincipal CustomUserDetails seller) {
        alarmSellerService.updateAlarm(updateSellerAlarmDto, seller.getId());

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 변경됐습니다.")
                .build();
    }

    private final AlarmSendService alarmSendService;

    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PostMapping("test")
    public ApiResponseEntity<?> test(@ModelAttribute SendSellerTextDto sendSellerTextDto) {
        alarmSendService.sendSellerAlarm(1L, "sindong942@naver.com", "01088417145", sendSellerTextDto);

        return ApiResponseEntity.builder()
                .message("알림 설정이 성공적으로 변경됐습니다.")
                .build();
    }

}
