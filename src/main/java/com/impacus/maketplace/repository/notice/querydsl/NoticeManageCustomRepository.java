package com.impacus.maketplace.repository.notice.querydsl;

import com.impacus.maketplace.dto.notice.NoticeManageDataDto;
import com.impacus.maketplace.dto.notice.NoticeManageSearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeManageCustomRepository {
    List<NoticeManageDataDto> findList(NoticeManageSearchDto noticeManageSearchDto, Pageable pageable);
}
