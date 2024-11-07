package com.impacus.maketplace.repository.notice.querydsl;

import com.impacus.maketplace.dto.notice.*;
import com.impacus.maketplace.entity.notice.Notice;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeCustomRepository {
    List<NoticeManageDataDto> findManageList(NoticeManageSearchDto noticeManageSearchDto, Pageable pageable);

    NoticeManageGetDto findManage(Long noticeId);

    List<NoticeShowDto> showNotice();

    List<Notice> findForStop();

    List<NoticeListDto> findList();

    NoticeGetDto find(Long noticeId);
}
