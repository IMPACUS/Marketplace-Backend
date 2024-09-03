package com.impacus.maketplace.controller.alarm.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.get.GetShoppingBenefitsDto;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.UpdateOrderDeliveryDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateShoppingBenefitsDto;
import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsEnum;
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
@RequestMapping("api/v1/alarm/shopping-benefits")
public class AlarmShoppingBenefitsController {
    private final AlarmService alarmService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<?> addShoppingBenefits(@RequestBody AddShoppingBenefitsDto saveShoppingBenefitsDto,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.add(saveShoppingBenefitsDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 저장이 성공적으로 됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<?> getShoppingBenefits(@RequestParam("content") ShoppingBenefitsEnum content,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        GetShoppingBenefitsDto readDto = (GetShoppingBenefitsDto) alarmService.find(content, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 조회가 성공적으로 됐습니다.")
                .data(readDto)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("{alarmId}")
    public ApiResponseEntity<?> updateShoppingBenefits(@PathVariable("alarmId") Long id,
                                                       @RequestBody UpdateShoppingBenefitsDto updateShoppingBenefitsDto,
                                                       @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.update(id, updateShoppingBenefitsDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 수정이 성공적으로 됐습니다.")
                .build();
    }
}
