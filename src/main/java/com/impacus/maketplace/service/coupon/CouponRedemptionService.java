package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.TriggerType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponIssuanceHistory;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.coupon.CouponIssuanceHistoryRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 사용자가 쿠폰을 사용하고, 시스템이 쿠폰을 지급하는 로직이 담겨있는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponRedemptionService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponIssuanceHistoryRepository couponIssuanceHistoryRepository;

    @Transactional
    public void issueCouponTargetUserByAdmin(Long couponId, User user) {

        // 1. 등록되어 있는 쿠폰 조회
        Coupon coupon = couponRepository.findWriteLockById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 쿠폰 상태 확인
        // 2.1 삭제된 쿠폰인지 확인
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        // 2. 쿠폰 발급하기
        // 2.1 해당 사용자에게 발급 + 발급 횟수 증가
        UserCoupon userCoupon = createIssueCouponByAdmin(user.getId(), coupon);
        userCouponRepository.save(userCoupon);


        // 2.2 쿠폰 발급 이력 기록
        CouponIssuanceHistory couponIssuanceHistory = createCouponIssuanceHistory(userCoupon.getId(), user.getId(), TriggerType.ADMIN);
        couponIssuanceHistoryRepository.save(couponIssuanceHistory);
    }

    @Transactional
    public void issueCouponAllUserByAdmin(Long couponId, UserLevel userLevel) {

        // 1. 등록되어 있는 쿠폰 조회

        // 2.1 레벨이 있을 경우 레벨에 맞는 유저 조회
        // 2.2 레벨이 null일 경우 모든 유저 조회

        // 3. 삭제되지 않은 유저에 한해서 쿠폰 발급

        // 4. 발급 이력 기록하기
    }

    /**
     * 쿠폰 발급 수 업데이트 후 쿠폰 발급
     * @param userId 유저 PK
     * @param coupon 쿠폰 Entity
     * @return
     */
    private UserCoupon createIssueCouponByAdmin(Long userId, Coupon coupon) {
        coupon.updateQuantityIssued(1);
        return UserCoupon.builder()
                .userId(userId)
                .couponId(coupon.getId())
                .availableDownloadAt(LocalDateTime.now())
                .isDownload(false)
                .downloadAt(null)
                .isUsed(false)
                .usedAt(null)
                .expiredAt(null)
                .status(UserCouponStatus.ISSUE_SUCCESS)
                .build();
    }

    private CouponIssuanceHistory createCouponIssuanceHistory(Long userCouponId, Long userId, TriggerType triggerType) {
        return CouponIssuanceHistory.builder()
                .userId(userId)
                .userCouponId(userCouponId)
                .triggerType(triggerType)
                .build();
    }
}
