package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeShowDto {
    private Long noticeId;
    private String imagePath;
    private String comment;

    public NoticeShowDto(Long id, String imagePath, LocalDateTime startDateTime, NoticeType type) {
        this.noticeId = id;
        this.imagePath = imagePath;
        int year = startDateTime.getYear();
        int month = startDateTime.getMonthValue();
        int day = startDateTime.getDayOfMonth();
        String typeStr = type.equals(NoticeType.NOTICE) ? "공지사항이" : "이벤트가";
        this.comment = year + "년 " + month + "월 " + day + "일 " + typeStr + " 등록 되었습니다.";
    }
}
