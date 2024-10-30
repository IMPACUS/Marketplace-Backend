package com.impacus.maketplace.dto.notice;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class NoticeManageListDto {
    private Integer allCount;
    private Integer eventCount;
    private Integer NoticeCount;
    private Page<NoticeManageDataDto> data;
}
