package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponIssuanceHistory;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.repository.coupon.CouponIssuanceHistoryRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 시스템이 쿠폰을 지급하는 로직이 담겨있는 서비스
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssuanceManager {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponIssuanceHistoryRepository couponIssuanceHistoryRepository;

    @Transactional
    public UserCoupon issueCouponToUser(Long userId, Long couponId, TriggerType triggerType) {

        List<Long> userIds = List.of(userId);

        // 1. 쿠폰 발급 함수 호출
        List<UserCoupon> userCoupons = issueCouponToUsers(userIds, couponId, triggerType);

        return userCoupons.get(0);
    }

    /**
     * <h3>사용자들에게 쿠폰 발급</h3>
     */
    @Transactional
    public List<UserCoupon> issueCouponToUsers(List<Long> userIds, Long couponId, TriggerType triggerType) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findWriteLockById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 최종 조건 확인
        if (!triggerType.equals(TriggerType.ADMIN) && !isWithinQuota(coupon)) {
            throw new CustomException(CouponErrorType.END_FIRST_COUNT_COUPON);
        }

        // 3. 모든 사용자에게 발급 + 발급 횟수 증가
        List<UserCoupon> userCoupons = userIds.stream()
                .map((id) -> issueCoupon(id, coupon)).toList();
        userCouponRepository.saveAll(userCoupons);

        // 4. 쿠폰 발급 이력 기록
        List<CouponIssuanceHistory> couponIssuanceHistoryList = userCoupons.stream()
                .map((userCoupon) -> createCouponIssuanceHistory(userCoupon.getUserId(), userCoupon.getId(), triggerType)).toList();
        couponIssuanceHistoryRepository.saveAll(couponIssuanceHistoryList);

        return userCoupons;
    }

    private boolean isWithinQuota(Coupon coupon) {
        return !(coupon.getPaymentTarget().equals(PaymentTarget.FIRST) && coupon.getQuantityIssued() >= coupon.getFirstCount());
    }

    /**
     * 쿠폰 발급 수 업데이트 후 쿠폰 발급
     *
     * @param userId 유저 PK
     * @param coupon 쿠폰 Entity
     * @param daysUntilAvailable 몇 일 뒤에 쿠폰을 사용할 수 있는지
     * @return UserCoupon
     */
    private UserCoupon issueCoupon(Long userId, Coupon coupon) {
        LocalDate expiredAt = coupon.getExpireTimeType() == ExpireTimeType.LIMIT ?
                LocalDate.now().plusDays(coupon.getExpireTimeDays()) : null;
        coupon.updateQuantityIssued(1);

        LocalDate availableDownloadAt = LocalDate.now().plusDays(coupon.getIssuedTimeType() == IssuedTimeType.IMMEDIATE ? 0 : 7);

        return UserCoupon.builder()
                .userId(userId)
                .couponId(coupon.getId())
                .availableDownloadAt(availableDownloadAt)
                .isDownload(false)
                .downloadAt(null)
                .isUsed(false)
                .usedAt(null)
                .expiredAt(expiredAt)
                .status(UserCouponStatus.ISSUE_SUCCESS)
                .build();
    }

    private CouponIssuanceHistory createCouponIssuanceHistory(Long userId, Long userCouponId, TriggerType triggerType) {
        return CouponIssuanceHistory.builder()
                .userId(userId)
                .userCouponId(userCouponId)
                .triggerType(triggerType)
                .build();
    }
}
