package com.impacus.maketplace.service.notice;

import com.impacus.maketplace.dto.notice.NoticeGetDto;
import com.impacus.maketplace.dto.notice.NoticeListDto;
import com.impacus.maketplace.dto.notice.NoticeShowDto;
import com.impacus.maketplace.entity.notice.Notice;
import com.impacus.maketplace.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeShowDto> showNotice() {
        return noticeRepository.showNotice();
    }

    public void increaseImpression(List<Long> ids) {
        List<Notice> list = noticeRepository.findAllById(ids);
        for (Notice n : list) n.increaseImpression();
    }

    public List<NoticeListDto> findList() {
        return noticeRepository.findList();
    }

    public NoticeGetDto find(Long noticeId) {
        return noticeRepository.find(noticeId);
    }
}
