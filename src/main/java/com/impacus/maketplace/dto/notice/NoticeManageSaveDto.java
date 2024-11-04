package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.entity.notice.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class NoticeManageSaveDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "내용은 null 허용안됩니다.")
    private String content;

    private MultipartFile image;

    @NotNull(message = "타입에 null 허용안됩니다.")
    private NoticeType type;

    @NotNull(message = "시작 날짜에 null 허용안됩니다.")
    private LocalDate startDate;

    @NotNull(message = "끝 날짜에 null 허용안됩니다.")
    private LocalDate endDate;

    @NotNull(message = "시작 시간에 null 허용안됩니다.")
    @Range(min = 0, max = 23, message = "0시 ~ 23시까지 입력가능합니다.")
    private Integer startTime;

    @NotNull(message = "끝 시간간에 null 용안됩니다.")
    @Range(min = 0, max = 23, message = "0시 ~ 23시까지 입력가능합니다.")
    private Integer endTime;

    public Notice toEntity(Long attachFileId) {
        return new Notice(this, attachFileId);
    }
}
