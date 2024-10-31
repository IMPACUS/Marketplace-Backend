package com.impacus.maketplace.entity.notice;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.dto.notice.NoticeManageSaveDto;
import com.impacus.maketplace.dto.notice.NoticeManageUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {
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

    public Notice(NoticeManageSaveDto n, Long attachFileId) {
        this.type = n.getType();
        this.attachFileId = attachFileId;
        this.title = n.getTitle();
        this.content = n.getContent();
        this.status = NoticeStatus.RUN;
        this.startDateTime = n.getStartDate().atTime(n.getStartTime(), 0);
        this.endDateTime = n.getEndDate().atTime(n.getEndTime(), 0);
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public void update(NoticeManageUpdateDto n, Long attachFileId) {
        this.attachFileId = attachFileId;
        this.title = n.getTitle();
        this.content = n.getContent();
        this.status = n.getStatus();
        this.startDateTime = n.getStartDate().atTime(n.getStartTime(), 0);
        this.endDateTime = n.getEndDate().atTime(n.getEndTime(), 0);
    }

    public void increaseImpression() {
        this.impression = this.impression + 1;
    }
}
