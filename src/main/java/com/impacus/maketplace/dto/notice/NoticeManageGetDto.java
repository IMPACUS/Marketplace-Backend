package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@Data
public class NoticeManageGetDto {
    private Long noticeId;
    private String imagePath;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer startTime;
    private Integer endTime;
    private NoticeStatus status;

    public NoticeManageGetDto(Long noticeId, String imagePath, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime, NoticeStatus status) {
        this.noticeId = noticeId;
        this.imagePath = imagePath;
        this.title = title;
        this.content = content;
        this.startDate = startDateTime.toLocalDate();
        this.endDate = endDateTime.toLocalDate();
        this.startTime = startDateTime.getHour();
        this.endTime = endDateTime.getHour();
        this.status = status;
    }
}
