package com.impacus.maketplace.service.consumer;

import com.impacus.maketplace.common.enumType.StatisticsPeriod;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.entity.consumer.ConsumerDailyVisitors;
import com.impacus.maketplace.entity.consumer.ConsumerHourlyVisitors;
import com.impacus.maketplace.repository.consumer.ConsumerDailyVisitorsRepository;
import com.impacus.maketplace.repository.consumer.ConsumerHourlyVisitorsRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ConsumerVisitorsScheduler {

    private final UserRepository userRepository;
    private final ConsumerDailyVisitorsRepository consumerDailyVisitorsRepository;
    private final ConsumerHourlyVisitorsRepository consumerHourlyVisitorsRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void saveDailyVisitors() {
        try {
            LocalDate now = LocalDate.now().minusDays(1);
            long visitors = userRepository.countByRecentLoginAtBetweenAndType(
                    now.atStartOfDay(),
                    now.atTime(LocalTime.MAX),
                    UserType.ROLE_CERTIFIED_USER);

            ConsumerDailyVisitors consumerVisitors = ConsumerDailyVisitors.toEntity(visitors);
            consumerDailyVisitorsRepository.save(consumerVisitors);

            LogUtils.writeInfoLog("saveDailyVisitors",
                    "Save daily visitors " + visitors);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("saveDailyVisitors",
                    "Fail to save daily visitors", ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0,5,9,13,17,21 * * *")
    public void saveHourlyVisitors() {
        try {
            LocalDate now = LocalDate.now();
            int hour = LocalTime.now().getHour();
            int previousHour = StatisticsPeriod.YESTERDAY.getPreviousStatisticsValue(hour);

            LocalDateTime endAt = now.atTime(hour, 0);
            long visitors = userRepository.countByRecentLoginAtBetweenAndType(
                    endAt.minusHours(hour - previousHour),
                    now.atTime(hour, 0),
                    UserType.ROLE_CERTIFIED_USER);

            ConsumerHourlyVisitors consumerHourlyVisitors = ConsumerHourlyVisitors.toEntity(hour, visitors);
            consumerHourlyVisitorsRepository.save(consumerHourlyVisitors);

            LogUtils.writeInfoLog("saveHourlyVisitors",
                    "Save hourly visitors " + visitors);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("saveHourlyVisitors",
                    "Fail to save hourly visitors", ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteHourlyVisitors() {
        try {
            LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
            int visitors = consumerHourlyVisitorsRepository.deleteByVisitTimeBefore(oneMonthAgo);

            LogUtils.writeInfoLog("deleteHourlyVisitors",
                    "Delete hourly visitors " + visitors);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("deleteHourlyVisitors",
                    "Fail to delete hourly visitors", ex);
        }
    }
}
