package com.impacus.maketplace.service.alarm;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.common.utils.AmountComma;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.point.AlarmPointDTO;
import com.impacus.maketplace.entity.alarm.admin.AlarmAdminForUser;
import com.impacus.maketplace.entity.alarm.seller.AlarmHold;
import com.impacus.maketplace.entity.alarm.user.AlarmUser;
import com.impacus.maketplace.repository.alarm.admin.AlarmAdminForUserRepository;
import com.impacus.maketplace.repository.alarm.seller.AlarmHoldRepository;
import com.impacus.maketplace.repository.alarm.user.AlarmUserRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponApiRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmScheduleService {
    private final AlarmSendService alarmSendService;
    private final AlarmUserRepository alarmUserRepository;
    private final AlarmAdminForUserRepository alarmAdminForUserRepository;
    private final AlarmHoldRepository alarmHoldRepository;
    private final GreenLabelPointAllocationRepository greenLabelPointAllocationRepository;
    private final CouponApiRepository couponApiRepository;

    @Scheduled(cron = "0 0/30 * * * *", zone = "Asia/Seoul")
    public void sendHoldAlarm() {
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

    @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Seoul")
    public void sendCouponAndPoint() {
        this.sendCouponAlarm();
        this.sendPointAlarm();
    }

    private void sendCouponAlarm() {
        List<AlarmCouponDTO> alarmCoupons = couponApiRepository.getAlarmCoupons();
        Map<Long, List<AlarmCouponDTO>> alarmMap = alarmCoupons.stream().collect(Collectors.groupingBy(AlarmCouponDTO::getUserId));
        for (Long userId : alarmMap.keySet()) {
            Optional<AlarmUser> optional = alarmUserRepository.findByUserIdAndCategory(userId, AlarmUserCategoryEnum.SHOPPING_BENEFITS);
            if (optional.isEmpty()) log.error("쿠폰 알림 전송중 해당 유저 id는 존재하지 않습니다. userId : {}", userId);
            else {
                AlarmUser alarmUser = optional.get();
                if (alarmUser.getIsOn()) {
                    Boolean isMsg = alarmUser.getMsg();
                    Boolean isKakao = alarmUser.getKakao();
                    Boolean isEmail = alarmUser.getEmail();
                    Boolean isPush = alarmUser.getPush();
                    List<AlarmCouponDTO> alarmCouponDTOS = alarmMap.get(userId);
                    String receiver = StringUtils.getEmailInfo(alarmCouponDTOS.get(0).getEmail()).getEmail();
                    String userName = alarmCouponDTOS.get(0).getUserName();
                    String phone = alarmCouponDTOS.get(0).getPhoneNumber().replace("-", "");

                    int[] days = {1, 30};
                    for (int day : days) {
                        List<AlarmCouponDTO> couponList = alarmCouponDTOS.stream().filter(a -> LocalDate.now().plusDays(day).isEqual(a.getExpiredAt())).collect(Collectors.toList());
                        if (couponList.size() != 0) {
                            String expiredAt = couponList.get(0).getExpiredAt().plusDays(1).toString() + " 00:00:00";
                            String couponName = couponList.stream()
                                    .map(AlarmCouponDTO::getCouponName)
                                    .collect(Collectors.joining(", "));
                            String amount = couponList.stream()
                                    .map(a -> AmountComma.formatCurrency(a.getBenefitValue()) + a.getBenefitType().getValue())
                                    .collect(Collectors.joining(", "));

                            AlarmUserSubcategoryEnum couponExtinction;
                            if (day == 1) couponExtinction = AlarmUserSubcategoryEnum.COUPON_EXTINCTION_2;
                            else couponExtinction = AlarmUserSubcategoryEnum.COUPON_EXTINCTION_1;

                            String template = couponExtinction.getTemplate();
                            String subject = couponExtinction.getValue();
                            String kakaoCode = couponExtinction.getKakaoCode();
                            template = template.replace("#{홍길동}", userName).replace("#{쿠폰명}", couponName).replace("#{쿠폰금액}", amount).replace("#{유효기간}", expiredAt);
                            alarmSendService.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, template);
                        }
                    }
                }
            }
        }

    }

    private void sendPointAlarm() {
        LocalDateTime now = LocalDateTime.now();
        List<AlarmAdminForUser> shoppingBenefits = alarmAdminForUserRepository.findByCategory(AlarmUserCategoryEnum.SHOPPING_BENEFITS);
        List<AlarmPointDTO> allAlarmPoint = greenLabelPointAllocationRepository.findAllAlarmPoint();
        for (AlarmPointDTO dto : allAlarmPoint) {
            LocalDateTime expiredAt = dto.getExpiredAt();
            long daysBetween = ChronoUnit.DAYS.between(now, expiredAt);

            if ((daysBetween == 1) || (daysBetween == 30)) {
                Optional<AlarmUser> optional = alarmUserRepository.findByUserIdAndCategory(dto.getUserId(), AlarmUserCategoryEnum.SHOPPING_BENEFITS);
                if (optional.isEmpty()) log.error("포인트 알림 전송중 해당 유저 id는 존재하지 않습니다. userId : {}", dto.getUserId());
                else {
                    AlarmUser alarmUser = optional.get();
                    if (alarmUser.getIsOn()) {
                        Boolean isMsg = alarmUser.getMsg();
                        Boolean isKakao = alarmUser.getKakao();
                        Boolean isEmail = alarmUser.getEmail();
                        Boolean isPush = alarmUser.getPush();
                        String userName = dto.getUserName();
                        String receiver = StringUtils.getEmailInfo(dto.getEmail()).getEmail();
                        String phone = dto.getPhoneNumber().replace("-", "");
                        String remainPoint = AmountComma.formatCurrency(dto.getRemainPoint());
                        String expiredStr = removeAfterDot(expiredAt.toString().replace("T", " "));

                        AlarmUserSubcategoryEnum pointExtinction;
                        if (daysBetween == 1) pointExtinction = AlarmUserSubcategoryEnum.POINT_EXTINCTION_2;
                        else pointExtinction = AlarmUserSubcategoryEnum.POINT_EXTINCTION_1;

                        String kakaoCode = pointExtinction.getKakaoCode();
                        String subject = pointExtinction.getValue();
                        String template = pointExtinction.getTemplate()
                                .replace("#{홍길동}", userName)
                                .replace("#{적립금}", remainPoint)
                                .replace("#{유효기간}", expiredStr);
                        alarmSendService.sendAlarm(isKakao, isEmail, isMsg, subject, receiver, phone, kakaoCode, template);
                    }
                }
            }
        }
    }

    private String removeAfterDot(String str) {
        int index = str.indexOf(".");
        if (index != -1) {  // .이 문자열에 존재할 경우
            return str.substring(0, index);
        }
        return str;  // .이 없으면 원본 문자열 반환
    }
}

