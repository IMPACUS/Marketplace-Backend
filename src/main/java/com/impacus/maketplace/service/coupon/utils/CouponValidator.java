package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.CouponIssueType;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.CouponType;
import com.impacus.maketplace.common.enumType.coupon.PaymentTarget;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CouponValidator {

    private final UserCouponRepository userCouponRepository;

    /**
     * <h3>ADMIN이 사용자에게 쿠폰을 발급해주는 경우 조건</h3>
     * <p>1. 삭제 되지 않은 경우</p>
     */
    public void validateAdminIssuedCouponWithException(Coupon coupon) {
        // 1. 삭제 여부 확인
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }
    }

    /**
     * <h3>지급형 쿠폰 조건</h3>
     * <p>1. 쿠폰 공통 조건</p>
     * <p>2. 지급형 쿠폰</p>
     * <p>3. 1회성 쿠폰</p>
     * <p>4. 발급 받은 이력 X</p>
     */
    public void validateProvisionCouponWithException(Long userId, Coupon coupon) {
        // 1. 쿠폰 공통 조건 검증
        isCouponEligibleWithException(coupon);

        // 2. 쿠폰 형식 확인 - 지급형
        if (!coupon.getCouponType().equals(CouponType.PROVISION)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_EVENT_COUPON);
        }

        // 3. 쿠폰 발급 횟수 - 1회성
        if (!coupon.getCouponIssueType().equals(CouponIssueType.ONETIME)) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_PERSISTENCE_COUPON);
        }

        // 4. 발급 이력 검증
        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new CustomException(CouponErrorType.INVALID_REGISTER_ALREADY_ISSUE);
        }
    }


    /**
     * <h3>쿠폰 공통 조건</h3>
     * <p>
     * 1. 삭제되지 않은 경우
     * </p>
     * <p>
     * 2. 발급 상태가 중지가 아닌 경우
     * </p>
     * <p>
     * 3. 선착순 쿠폰이 아니거나 선착순 쿠폰인 경우 발급 수량에 여유가 있는 경우
     * </p>
     */
    private boolean isCouponEligible(Coupon coupon) {
        return !coupon.getIsDeleted()
                && !coupon.getStatusType().equals(CouponStatusType.STOP)
                && isWithinQuota(coupon);
    }

    private void isCouponEligibleWithException(Coupon coupon) {
        if (coupon.getIsDeleted()) {
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        if (coupon.getStatusType().equals(CouponStatusType.STOP)) {
            throw new CustomException(CouponErrorType.IS_STOP_COUPON);
        }

        if (!isWithinQuota(coupon)) {
            throw new CustomException(CouponErrorType.END_FIRST_COUNT_COUPON);
        }
    }

    private boolean isWithinQuota(Coupon coupon) {
        // 선착순 검증 로직
        return !(coupon.getPaymentTarget().equals(PaymentTarget.FIRST) &&  coupon.getQuantityIssued() >= coupon.getFirstCount());
    }
}
