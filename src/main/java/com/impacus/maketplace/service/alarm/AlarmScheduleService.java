package com.impacus.maketplace.service.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmScheduleService {
    private final AlarmSendService alarmSendService;

//    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Seoul")
//    public void run() {
//
//
//    }
}
