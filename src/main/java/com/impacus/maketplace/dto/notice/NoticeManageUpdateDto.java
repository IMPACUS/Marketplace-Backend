package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.common.enumType.notice.NoticeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoticeManageUpdateDto {
    @NotNull(message = "id는 null 허용 안됩니다.")
    private Long noticeId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "내용은 null 허용안됩니다.")
    private String content;

    private MultipartFile image;

    @NotNull(message = "시작 날짜에 null 허용안됩니다.")
    private LocalDate startDate;

    @NotNull(message = "끝 날짜에 null 허용안됩니다.")
    private LocalDate endDate;

    @NotNull(message = "시작 날짜에 null 허용안됩니다.")
    @Range(min = 0, max = 23, message = "0시 ~ 23시까지 입력가능합니다.")
    private Integer startTime;

    @NotNull(message = "끝 날짜에 null 허용안됩니다.")
    @Range(min = 0, max = 23, message = "0시 ~ 23시까지 입력가능합니다.")
    private Integer endTime;

    @NotNull(message = "상태에 null 허용안됩니다.")
    private NoticeStatus status;
}
