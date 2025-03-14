package com.impacus.maketplace.service.consumer;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.entity.consumer.ConsumerDailyVisitors;
import com.impacus.maketplace.repository.consumer.ConsumerDailyVisitorsRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ConsumerVisitorsScheduler {

    private final UserRepository userRepository;
    private final ConsumerDailyVisitorsRepository consumerDailyVisitorsRepository;

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
}
