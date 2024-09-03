package com.impacus.maketplace.controller.alarm.user;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.alarm.user.get.GetReviewDto;
import com.impacus.maketplace.dto.alarm.user.add.*;
import com.impacus.maketplace.dto.alarm.user.update.UpdateOrderDeliveryDto;
import com.impacus.maketplace.dto.alarm.user.update.UpdateReviewDto;
import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
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
@RequestMapping("api/v1/alarm/review")
public class AlarmReviewController {
    private final AlarmService alarmService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<?> addReview(@RequestBody AddReviewDto saveReviewDto,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.add(saveReviewDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 저장이 성공적으로 됐습니다.")
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<?> getReview(@RequestParam("content") ReviewEnum content,
                                          @AuthenticationPrincipal CustomUserDetails user) {
        GetReviewDto readDto = (GetReviewDto) alarmService.find(content, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 조회가 성공적으로 됐습니다.")
                .data(readDto)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("{alarmId}")
    public ApiResponseEntity<?> updateReview(@PathVariable("alarmId") Long id,
                                             @RequestBody UpdateReviewDto updateReviewDto,
                                             @AuthenticationPrincipal CustomUserDetails user) {
        alarmService.update(id, updateReviewDto, user.getId());
        return ApiResponseEntity.builder()
                .message("알림 수정이 성공적으로 됐습니다.")
                .build();
    }
}
