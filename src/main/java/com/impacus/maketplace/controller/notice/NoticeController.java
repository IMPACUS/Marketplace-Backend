package com.impacus.maketplace.controller.notice;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.notice.NoticeGetDto;
import com.impacus.maketplace.dto.notice.NoticeListDto;
import com.impacus.maketplace.dto.notice.NoticeShowDto;
import com.impacus.maketplace.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("show")
    public ApiResponseEntity<?> showNotice() {
        List<NoticeShowDto> noticeShowDtos = noticeService.showNotice();
        noticeService.increaseImpression(noticeShowDtos.stream().map(NoticeShowDto::getNoticeId).collect(Collectors.toList()));

        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트 팝업이 성공적으로 조회됐습니다.")
                .data(noticeShowDtos)
                .build();
    }

    @GetMapping("list")
    public ApiResponseEntity<?> getNoticeList() {
        List<NoticeListDto> list = noticeService.findList();
        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트 리스트가 성공적으로 조회됐습니다.")
                .data(list)
                .build();
    }

    @GetMapping("{noticeId}")
    public ApiResponseEntity<?> getNoticeList(@PathVariable("noticeId") Long noticeId) {
        NoticeGetDto noticeGetDto = noticeService.find(noticeId);
        return ApiResponseEntity.builder()
                .message("공지 또는 이벤트가 성공적으로 조회됐습니다.")
                .data(noticeGetDto)
                .build();
    }
}
