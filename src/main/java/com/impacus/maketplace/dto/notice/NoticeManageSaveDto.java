package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeType;
import com.impacus.maketplace.entity.notice.NoticeManage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoticeManageSaveDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    private String content;
    private MultipartFile image;

    @NotNull(message = "타입에 null 허용안됩니다.")
    private NoticeType type;

    @NotNull(message = "시작 날짜에 null 허용안됩니다.")
    private LocalDate startDate;

    @NotNull(message = "끝 날짜에 null 허용안됩니다.")
    private LocalDate endDate;

    private LocalTime startTime;
    private LocalTime endTime;

    public NoticeManage toEntity(){
        return new NoticeManage(this);
    }
}
