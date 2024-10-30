package com.impacus.maketplace.service.notice;

import com.impacus.maketplace.common.enumType.notice.NoticeStatus;
import com.impacus.maketplace.entity.alarm.seller.AlarmHold;
import com.impacus.maketplace.entity.notice.Notice;
import com.impacus.maketplace.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NoticeScheduleService {
    private final NoticeRepository noticeRepository;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void statusToStop() {
        List<Notice> forStop = noticeRepository.findForStop();
        for (Notice n : forStop) n.setStatus(NoticeStatus.STOP);
    }
}
