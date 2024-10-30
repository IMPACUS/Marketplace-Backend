package com.impacus.maketplace.entity.notice;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.dto.notice.NoticeManageSaveDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "notice_manage")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeManage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType type;

    private Long attachFileId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeStatus status;

    @Column(nullable = false)
    private Integer impression = 0;

    public NoticeManage(NoticeManageSaveDto n) {
        this.type = n.getType();
        this.title = n.getTitle();
        this.content = n.getContent();
        this.status = NoticeStatus.RUN;
        this.startDateTime = n.getStartDate().atStartOfDay();
        this.endDateTime = n.getEndDate().atStartOfDay();

        LocalTime startTime = n.getStartTime();
        if (startTime != null) this.startDateTime = this.startDateTime.toLocalDate().atTime(startTime);
        LocalTime endTime = n.getEndTime();
        if (endTime != null) this.endDateTime = this.endDateTime.toLocalDate().atTime(endTime);
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }
}
