package com.impacus.maketplace.controller.alarm.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.get.GetBrandShopDto;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.UpdateBrandShopDto;
import com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum;
import com.impacus.maketplace.service.alarm.user.AlarmSendService;
import com.impacus.maketplace.service.alarm.user.AlarmService;
import com.impacus.maketplace.service.alarm.user.enums.AlarmEnum;
import com.impacus.maketplace.service.alarm.user.enums.AlarmKakaoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm/brand-shop")
public class AlarmBrandShopController {
    private final AlarmService alarmService;
    private final AlarmSendService alarmSendService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<?> addBrandShop(@RequestBody AddBrandShopDto saveBrandShopDto,
                                             @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.add(saveBrandShopDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 저장이 성공적으로 됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<?> getBrandShop(@RequestParam("content") BrandShopEnum content,
                                             @AuthenticationPrincipal CustomUserDetails user) {
        GetBrandShopDto readDto = (GetBrandShopDto) alarmService.find(content, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 조회가 성공적으로 됐습니다.")
                .data(readDto)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("{alarmId}")
    public ApiResponseEntity<?> updateBrandShop(@PathVariable("alarmId") Long id,
                                                @RequestBody UpdateBrandShopDto updateBrandShopDto,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.update(id, updateBrandShopDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 수정이 성공적으로 됐습니다.")
                .build();
    }

    @PostMapping("mail")
    public ApiResponseEntity<?> mailTest(@RequestParam("a") String a,@RequestParam("b") String b,@RequestParam("c") String c,@RequestParam("d") String d,@RequestParam("e") String e) {
        alarmSendService.sendKakao("01088417145", AlarmKakaoEnum.CANCEL, a, b, c, d, e);

        return ApiResponseEntity.builder()
                .message("메일 전송")
                .build();
    }
}
