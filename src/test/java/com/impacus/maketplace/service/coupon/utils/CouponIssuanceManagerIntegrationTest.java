package com.impacus.maketplace.service.coupon.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.impacus.maketplace.common.enumType.coupon.AutoManualType;
import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.CouponIssueType;
import com.impacus.maketplace.common.enumType.coupon.CouponProductType;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.CouponType;
import com.impacus.maketplace.common.enumType.coupon.CoverageType;
import com.impacus.maketplace.common.enumType.coupon.EventType;
import com.impacus.maketplace.common.enumType.coupon.ExpireTimeType;
import com.impacus.maketplace.common.enumType.coupon.IssuedTimeType;
import com.impacus.maketplace.common.enumType.coupon.PaymentTarget;
import com.impacus.maketplace.common.enumType.coupon.PeriodType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.impacus.maketplace.common.enumType.coupon.TriggerType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponIssuanceHistory;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("쿠폰 발행 매니저 테스트")
class CouponIssuanceManagerIntegrationTest {

    @Autowired
    private CouponIssuanceManager couponIssuanceManager;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    @Rollback(value = false)
    void setup() {
        entityManager.createQuery("delete from UserCoupon x").executeUpdate();
        entityManager.createQuery("delete from CouponIssuanceHistory x").executeUpdate();
        entityManager.createQuery("delete from Coupon x").executeUpdate();
        entityManager.flush();
    }

    @Test
    @DisplayName("한 명의 사용자에게 쿠폰이 올바르게 발급된다.")
    @Transactional
    void issueCouponToSingleUser() {
        // given
        Long couponId = saveMockCoupon();

        Long userId = 1L;

        // when
        Long userCouponId = couponIssuanceManager.issueCouponToUser(userId, couponId,
            TriggerType.ORDER).getId();

        entityManager.flush();
        entityManager.clear();

        UserCoupon findUserCoupon = entityManager.find(UserCoupon.class, userCouponId);
        Coupon findCoupon = entityManager.find(Coupon.class, couponId);

        // then
        List<CouponIssuanceHistory> couponIssuanceHistories = entityManager.createQuery(
                "SELECT c from CouponIssuanceHistory c WHERE c.userCouponId = (:userCouponId)",
                CouponIssuanceHistory.class).setParameter("userCouponId", findUserCoupon.getId())
            .getResultList();

        assertThat(couponIssuanceHistories.size()).isEqualTo(1);
        assertThat(couponIssuanceHistories.get(0).getUserId()).isEqualTo(userId);

        assertThat(findUserCoupon.getCouponId()).isEqualTo(couponId);
        assertThat(findCoupon.getQuantityIssued()).isEqualTo(1L);
        assertThat(findCoupon.getStatusType()).isEqualTo(CouponStatusType.ISSUED);
    }

    @Test
    @DisplayName("여러 명의 사용자에게 쿠폰이 올바르게 발급된다.")
    @Transactional
    void issueCouponToMultipleUsers() {
        // given
        Long couponId = saveMockCoupon();

        List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L);

        // when
        couponIssuanceManager.issueCouponToUsers(userIds, couponId, TriggerType.ORDER);

        entityManager.flush();
        entityManager.clear();

        // then
        for (long i = 1L; i <= 5L; i++) {
            UserCoupon userCoupon = entityManager.createQuery(
                    "SELECT uc from UserCoupon uc WHERE uc.userId = (:userId)", UserCoupon.class)
                .setParameter("userId", i).getSingleResult();

            Coupon coupon = entityManager.find(Coupon.class, couponId);

            Optional<CouponIssuanceHistory> couponIssuanceHistoryByUserCouponOpt = findCouponIssuanceHistoryByUserCoupon(
                userCoupon.getId(), i);

            assertThat(couponIssuanceHistoryByUserCouponOpt.isPresent()).isTrue();

            assertThat(userCoupon.getCouponId()).isEqualTo(couponId);
            assertThat(coupon.getQuantityIssued()).isEqualTo(5L);
            assertThat(coupon.getStatusType()).isEqualTo(CouponStatusType.ISSUED);
        }
    }

    @Test
    @DisplayName("여러 명의 사용자가 서로 다른 스레드에서 쿠폰을 발급받을 때 올바르게 동작되다.")
    @Transactional
    void issueCouponToMultipleUsersInDifferentThreads()
        throws ExecutionException, InterruptedException {
        // given
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Long couponId = saveMockCoupon();
        TestTransaction.flagForCommit();
        TestTransaction.end();


        List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L);

        // when
        List<CompletableFuture<Long>> completableFutures = userIds.stream().map(
            userId -> CompletableFuture.supplyAsync(
                () -> couponIssuanceManager.issueCouponToUser(userId, couponId,
                        TriggerType.ORDER)
                    .getId(), executor)).toList();

        List<Long> userCouponIds = completableFutures.stream().map(CompletableFuture::join)
            .toList();

        // then
        TestTransaction.start();
        Coupon coupon = entityManager.find(Coupon.class, couponId);
        assertThat(coupon.getQuantityIssued()).isEqualTo(5L);
        assertThat(coupon.getStatusType()).isEqualTo(CouponStatusType.ISSUED);

        for (Long userCouponId : userCouponIds) {
            UserCoupon userCoupon = entityManager.find(UserCoupon.class, userCouponId);

            Optional<CouponIssuanceHistory> couponIssuanceHistoryByUserCouponOpt = findCouponIssuanceHistoryByUserCoupon(
                userCoupon.getId(), userCoupon.getUserId());

            assertThat(couponIssuanceHistoryByUserCouponOpt.isPresent()).isTrue();

            assertThat(userCoupon.getCouponId()).isEqualTo(couponId);
        }
    }

    @Test
    @DisplayName("여러 사용자가 쿠폰을 발급하려는 상황에서 발행 수가 제한되어 있는 쿠폰을 발급받아 발급 수 제한을 넘어설 경우 예외를 발생시킨다.")
    @Transactional
    void issueCouponExceedingLimitThrowsException()
        throws ExecutionException, InterruptedException, TimeoutException {
        // given
        Long couponId = saveMockCoupon();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // when
        List<Future<UserCoupon>> list = userIds.stream().map(userId -> executor.submit(() -> couponIssuanceManager.issueCouponToUser(userId, couponId,
            TriggerType.ORDER))).toList();

        int exceptionCnt = 0;
        for (Future<UserCoupon> future : list) {
            try {
                future.get(5L, TimeUnit.SECONDS);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof CustomException) {
                    if (((CustomException) cause).getErrorType() == CouponErrorType.END_FIRST_COUNT_COUPON) {
                        exceptionCnt++;
                    }
                }
            }
        }

        // then
        assertThat(exceptionCnt).isEqualTo(5);

        TestTransaction.start();
        Coupon coupon = entityManager.createQuery("select c from Coupon c where c.id = :couponId",
            Coupon.class).setParameter("couponId", couponId).getSingleResult();

        assertThat(coupon.getQuantityIssued()).isEqualTo(5);
        assertThat(coupon.getStatusType()).isEqualTo(CouponStatusType.ISSUED);

        List<UserCoupon> userCoupons = findUserCouponByUserIds(userIds);

        assertThat(userCoupons.size()).isEqualTo(5);

        userCoupons.forEach(userCoupon -> {
            Optional<CouponIssuanceHistory> optional = findCouponIssuanceHistoryByUserCoupon(
                userCoupon.getId(), userCoupon.getUserId());

            assertThat(optional.isPresent()).isTrue();
        });
        TestTransaction.end();
    }

    private List<UserCoupon> findUserCouponByUserIds(List<Long> userIds) {
        return entityManager.createQuery("select u from UserCoupon u where u.userId in (:userIds)",
            UserCoupon.class).setParameter("userIds", userIds).getResultList();
    }


    private Optional<CouponIssuanceHistory> findCouponIssuanceHistoryByUserCoupon(Long userCouponId,
        Long userId) {
        List<CouponIssuanceHistory> queryResult = entityManager.createQuery(
                "SELECT c from CouponIssuanceHistory c WHERE c.userCouponId = (:userCouponId) and c.userId = (:userId)",
                CouponIssuanceHistory.class).setParameter("userCouponId", userCouponId)
            .setParameter("userId", userId).getResultList();

        if (queryResult.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(queryResult.get(0));
    }

    private Long saveMockCoupon() {
        Coupon coupon = createCouponWithRemainingCountOf5();
        Long couponId = couponRepository.save(coupon).getId();
        entityManager.flush();
        entityManager.clear();
        return couponId;
    }

    private Coupon createCouponWithRemainingCountOf5() {
        return Coupon.builder().code("TEST_CODE").name("TEST_NAME").description("TEST_DESCRIPTION")
            .benefitType(BenefitType.AMOUNT).benefitValue(10000L).productType(CouponProductType.ALL)
            .paymentTarget(PaymentTarget.FIRST).firstCount(5).quantityIssued(0L)
            .issuedTimeType(IssuedTimeType.IMMEDIATE).couponType(CouponType.EVENT)
            .eventType(EventType.PAYMENT_ORDER).couponIssueType(CouponIssueType.ONETIME)
            .expireTimeType(ExpireTimeType.UNLIMITED).expireTimeDays(null)
            .issueCoverageType(CoverageType.ALL).issueCoverageSubCategoryName(null)
            .useCoverageType(CoverageType.ALL).useCoverageSubCategoryName(null)
            .useStandardType(StandardType.UNLIMITED).useStandardValue(null)
            .issueConditionType(StandardType.UNLIMITED).issueConditionValue(null)
            .periodType(PeriodType.UNSET).periodStartAt(null).periodEndAt(null).numberOfPeriod(null)
            .autoManualType(AutoManualType.AUTO).loginAlarm(false).emailAlarm(false)
            .kakaoAlarm(false).smsAlarm(false).statusType(CouponStatusType.ISSUING).isDeleted(true)
            .build();
    }
}