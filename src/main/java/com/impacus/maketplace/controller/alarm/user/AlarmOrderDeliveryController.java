package com.impacus.maketplace.controller.alarm.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.get.GetOrderDeliveryDto;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.UpdateBrandShopDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateOrderDeliveryDto;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;
import com.impacus.maketplace.service.alarm.user.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/order-delivery")
public class AlarmOrderDeliveryController {
    private final AlarmService alarmService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<?> addOrderDelivery(@RequestBody AddOrderDeliveryDto saveOrderDeliveryDto,
                                                 @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.add(saveOrderDeliveryDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 저장이 성공적으로 됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<?> getOrderDelivery(@RequestParam("content") OrderDeliveryEnum content,
                                                 @AuthenticationPrincipal CustomUserDetails user) {
        GetOrderDeliveryDto readDto = (GetOrderDeliveryDto) alarmService.find(content, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 조회가 성공적으로 됐습니다.")
                .data(readDto)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("{alarmId}")
    public ApiResponseEntity<?> updateOrderDelivery(@PathVariable("alarmId") Long id,
                                                    @RequestBody UpdateOrderDeliveryDto updateOrderDeliveryDto,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.update(id, updateOrderDeliveryDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 수정이 성공적으로 됐습니다.")
                .build();
    }
}
