package com.impacus.maketplace.controller.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.notice.*;
import com.impacus.maketplace.service.notice.NoticeManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notice/manage")
public class NoticeManageController {
    private final NoticeManageService noticeManageService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PostMapping("")
    public ApiResponseEntity<?> saveNotice(@Valid NoticeManageSaveDto n) {
        noticeManageService.timeValidation(n.getStartDate(), n.getEndDate(), n.getStartTime(), n.getEndTime());
        noticeManageService.add(n);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 저장됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @GetMapping("")
    public ApiResponseEntity<?> getNotice(@RequestParam("noticeId") Long noticeId) {
        NoticeManageGetDto noticeManageGetDto = noticeManageService.find(noticeId);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 조회됐습니다.")
                .data(noticeManageGetDto)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PutMapping("")
    public ApiResponseEntity<?> updateNotice(@Valid NoticeManageUpdateDto n) {
        noticeManageService.timeValidation(n.getStartDate(), n.getEndDate(), n.getStartTime(), n.getEndTime());
        noticeManageService.update(n);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 수정됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @GetMapping("list")
    public ApiResponseEntity<?> NoticeList(@ModelAttribute NoticeManageSearchDto noticeManageSearchDto,
                                           Pageable pageable) {
        NoticeManageListDto noticeManageListDto = noticeManageService.findList(noticeManageSearchDto, pageable);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 호출됐습니다.")
                .data(noticeManageListDto)
                .build();

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @PutMapping("status")
    public ApiResponseEntity<?> updateNoticeStatus(@RequestParam("status") NoticeStatus status,
                                                   @RequestParam("ids") List<Long> ids) {
        noticeManageService.updateStatus(ids, status);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트 상태가 성공적으로 변경됐습니다.")
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRINCIPAL_ADMIN', 'ROLE_OWNER')")
    @DeleteMapping("")
    public ApiResponseEntity<?> deleteNotice(@RequestParam("ids") List<Long> ids) {
        noticeManageService.delete(ids);

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 제거됐습니다.")
                .build();
    }
}
