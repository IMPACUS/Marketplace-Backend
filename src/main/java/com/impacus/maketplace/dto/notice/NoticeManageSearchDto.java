package com.impacus.maketplace.dto.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeType;
import lombok.Data;

@Data
public class NoticeManageSearchDto {
    private String search;
    private NoticeType type;
}
