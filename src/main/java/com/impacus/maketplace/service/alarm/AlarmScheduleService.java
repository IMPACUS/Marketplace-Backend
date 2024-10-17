package com.impacus.maketplace.service.alarm;

import com.impacus.maketplace.entity.alarm.seller.AlarmHold;
import com.impacus.maketplace.repository.alarm.seller.AlarmHoldRepository;
import com.impacus.maketplace.service.coupon.CouponApiServiceImpl;
import com.impacus.maketplace.service.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmScheduleService {
    private final AlarmSendService alarmSendService;
    private final AlarmHoldRepository alarmHoldRepository;
    private final PointService pointService;
    private final CouponApiServiceImpl couponApiService;


    @Scheduled(cron = "0 0/30 * * * *", zone = "Asia/Seoul")
    public void run() {
        List<AlarmHold> alarmHoldList = alarmHoldRepository.findAll();
        for (AlarmHold alarmHold : alarmHoldList) {
            LocalTime sendTime = alarmHold.getSendTime();
            LocalTime now = LocalTime.now();
            if (now.getHour() == sendTime.getHour() && now.getMinute() == sendTime.getMinute()) {
                Boolean isEmail = alarmHold.getEmail();
                Boolean isKakao = alarmHold.getKakao();
                Boolean isMsg = alarmHold.getMsg();
                String receiver = alarmHold.getReceiver();
                String subject = alarmHold.getSubject();
                String phone = alarmHold.getPhone();
                String kakaoCode = alarmHold.getKakaoCode();
                String text = alarmHold.getText();
                alarmSendService.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, text);
                alarmHoldRepository.deleteById(alarmHold.getId());
            }
        }
    }
}
