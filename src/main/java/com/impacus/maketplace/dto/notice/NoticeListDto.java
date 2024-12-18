package com.impacus.maketplace.dto.notice;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeListDto {
    private Long noticeId;
    private String title;
    private String content;
    private String startDate;

    public NoticeListDto(Long id, String title, String content, LocalDateTime startDateTime) {
        this.noticeId = id;
        this.title = title;
        this.content = content;
        String date = startDateTime.toLocalDate().toString().replace("-", ".");
        this.startDate = date + " " + startDateTime.getHour() + "시에 등록됨";
    }
}
