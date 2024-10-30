package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeManageDataDto {
    private Long noticeId;
    private String type;
    private String title;
    private String content;
    private String period;
    private String status;
    private Integer impression;

    public NoticeManageDataDto(Long id, NoticeType type, String title, String content, LocalDateTime startDate, LocalDateTime endDate, NoticeStatus status, Integer impression) {
        this.noticeId = id;
        this.type = type.getValue();
        this.title = title;
        this.content = content;
        String startStr = startDate.toLocalDate().toString().replace("-", ".");
        String endStr = endDate.toLocalDate().toString().replace("-", ".");
        this.period = startStr + " ~ " + endStr;
        this.status = status.getValue();
        this.impression = impression;
    }
}
