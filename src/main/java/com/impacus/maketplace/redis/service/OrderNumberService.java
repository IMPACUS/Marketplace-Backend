package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.OrderUtils;
import com.impacus.maketplace.redis.entity.OrderNumber;
import com.impacus.maketplace.redis.repository.OrderNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderNumberService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OrderNumberRepository orderNumberRepository;
    private final String ORDER_NUMBER_LOCK_KEY = "orderNumber:lock";
    private final long LOCK_MAX_DURATION = 30 * 1000; // 락 유지 시간: 30초

    /**
     * 주문 번호 생성 및 저장
     * @return 생성된 주문 번호, 실패 시 null 반환
     */
    @Transactional
    public String generateAndSaveOrderNumber() {
        String orderNumber = null;
        int attempts = 0;
        long backoff = 100; // 초기 백오프 시간 (100ms)

        while (orderNumber == null && attempts < 5) {
            if (acquireLock()) {
                try {
                    do {
                        orderNumber = OrderUtils.generateOrderNumber();
                    } while (orderNumberRepository.existsByOrderNumber(orderNumber));

                    // 주문 번호를 Redis에 저장
                    orderNumberRepository.save(new OrderNumber(orderNumber));
                    break; // 주문 번호 생성 및 저장에 성공했으므로 루프 종료
                } finally {
                    releaseLock();
                }
            } else {
                // 락을 얻지 못했으므로 지수 백오프 적용
                try {
                    TimeUnit.MILLISECONDS.sleep(backoff);
                    backoff *= 2; // 다음 시도에서 백오프 시간을 두 배로 증가
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
                    return null; // 인터럽트 발생 시 null 반환하고 메소드 종료
                }
            }
            attempts++; // 시도 횟수 증가
        }

        if (orderNumber == null) {
            LogUtils.writeInfoLog("OrderNumberService",
                    "OrderNumber Generated Fail");
            // 모든 시도 후에도 주문 번호를 생성 및 저장하지 못한 경우
            throw new CustomException(OrderErrorType.FAILE_GENERATE_ORDER_NUMBER);
        }
        LogUtils.writeInfoLog("OrderNumberService",
                String.format("OrderNumber Generated Success try count: {%d}", attempts));

        return orderNumber;
    }

    /**
     * Redis 분산 락 획득
     * @return 락 획득 성공 여부
     */
    private boolean acquireLock() {
        Long currentTime = System.currentTimeMillis();
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(ORDER_NUMBER_LOCK_KEY, currentTime.toString(), Duration.ofMillis(LOCK_MAX_DURATION));
        return Boolean.TRUE.equals(acquired);
    }

    /**
     * Redis 분산 락 해제
     */
    private void releaseLock() {
        redisTemplate.delete(ORDER_NUMBER_LOCK_KEY);
    }
}
