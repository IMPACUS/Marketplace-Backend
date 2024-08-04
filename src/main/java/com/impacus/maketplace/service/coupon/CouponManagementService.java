package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.response.UserCouponDownloadDTO;
import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
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

/**
 * 시스템이 쿠폰을 지급하는 로직이 담겨있는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponManagementService {

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
        UserCoupon userCoupon = issueInstantCoupon(user.getId(), coupon);
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

    @Transactional
    public UserCouponOverviewDTO registerCouponByUser(Long userId, String code) {
        // 1. 쿠폰 조회하기
        Coupon coupon = couponRepository.findWriteLockCouponByCode(code)
                .orElseThrow(() -> new CustomException(CouponErrorType.INVALID_COUPON_FORMAT));

        // 2. 쿠폰 등록 조건 검증
        validationInstantCouponCondition(userId, coupon);

        // 3. 쿠폰 발급하기
        // 3.1 해당 사용자에게 발급 + 발급 횟수 증가
        UserCoupon userCoupon = issueInstantCoupon(userId, coupon);
        userCouponRepository.save(userCoupon);

        // 3.2 쿠폰 발급 이력 기록
        CouponIssuanceHistory couponIssuanceHistory = createCouponIssuanceHistory(userCoupon.getId(), userId, TriggerType.REGISTER);
        couponIssuanceHistoryRepository.save(couponIssuanceHistory);

        return UserCouponOverviewDTO.builder()
                .couponId(userCoupon.getCouponId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .benefitType(coupon.getBenefitType())
                .benefitValue(coupon.getBenefitValue())
                .isDownload(userCoupon.getIsDownload())
                .downloadAt(userCoupon.getDownloadAt())
                .isUsed(userCoupon.getIsUsed())
                .usedAt(userCoupon.getUsedAt())
                .expiredAt(userCoupon.getExpiredAt())
                .availableDownloadAt(userCoupon.getAvailableDownloadAt())
                .build();
    }

    @Transactional
    public UserCouponDownloadDTO issueAndDownloadCoupon(Long userId, Long couponId) {

        // 1. 쿠폰 확인
        Coupon coupon = couponRepository.findWriteLockById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 쿠폰 발급 조건 확인
        validationInstantCouponCondition(userId, coupon);

        // 3. 쿠폰 발급하기
        // 3.1 해상 사용자에게 발급 + 발급 횟수 증가
        UserCoupon userCoupon = issueInstantCoupon(userId, coupon);
        userCoupon.setIsDownload(true);
        userCoupon.setDownloadAt(LocalDateTime.now());
        userCouponRepository.save(userCoupon);

        // 3.2 쿠폰 발급 이력 기록
        CouponIssuanceHistory couponIssuanceHistory = createCouponIssuanceHistory(userCoupon.getId(), userId, TriggerType.BRAND);
        couponIssuanceHistoryRepository.save(couponIssuanceHistory);

        return UserCouponDownloadDTO.builder()
                .userCouponId(userCoupon.getId())
                .isDownload(userCoupon.getIsDownload())
                .build();
    }
    private void validationInstantCouponCondition(Long userId, Coupon coupon) {
        // 1. 공통 검증 조건 확인(삭제 여부, 발급 상태, 선착순)
        validationCommonCondtion(coupon);

        // 2. 쿠폰 형식 확인 - 지급형만 가능
        if (!coupon.getCouponType().equals(CouponType.PROVISION)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_EVENT_COUPON);
        }

        // 3. 쿠폰 발급 횟수 - 1회성만 가능
        if (!coupon.getCouponIssueType().equals(CouponIssueType.ONETIME)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_PERSISTENCE_COUPON);
        }

        // 4. 발급 이력 검증
        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_ALREADY_ISSUE);
        }
    }

    private void validationCommonCondtion(Coupon coupon) {
        // 1. 삭제된 쿠폰 확인
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        // 2. 발급 상태 확인
        if (coupon.getStatusType().equals(CouponStatusType.STOP)) {
            throw new CustomException(CouponErrorType.IS_STOP_COUPON);
        }

        // 3. 선착순 확인
        if (coupon.getPaymentTarget().equals(PaymentTarget.FIRST)
                && coupon.getQuantityIssued() >= coupon.getFirstCount()) {
            throw new CustomException(CouponErrorType.END_FIRST_COUNT_COUPON);
        }
    }

    /**
     * 쿠폰 발급 수 업데이트 후 쿠폰 발급
     * @param userId 유저 PK
     * @param coupon 쿠폰 Entity
     * @return UserCoupon
     */
    private UserCoupon issueInstantCoupon(Long userId, Coupon coupon) {
        LocalDate expiredAt = coupon.getExpireTimeType() == ExpireTimeType.LIMIT ?
                LocalDate.now().plusDays(coupon.getExpireTimeDays()) : null;
        coupon.updateQuantityIssued(1);
        return UserCoupon.builder()
                .userId(userId)
                .couponId(coupon.getId())
                .availableDownloadAt(LocalDate.now())
                .isDownload(false)
                .downloadAt(null)
                .isUsed(false)
                .usedAt(null)
                .expiredAt(expiredAt)
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
