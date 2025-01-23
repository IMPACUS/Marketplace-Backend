package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.TriggerType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.response.UserCouponDownloadDTO;
import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.service.coupon.utils.CouponIssuanceManager;
import com.impacus.maketplace.service.coupon.utils.CouponValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 지급형 쿠폰 서비스
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvisionCouponService {

    private final CouponRepository couponRepository;
    private final CouponIssuanceManager couponIssuanceManager;
    private final CouponValidator couponValidator;

    /**
     * <h3>ADMIN이 특정 사용자에게 쿠폰 발급해주는 함수</h3>
     */
    public void issueCouponToUserByAdmin(Long userId, Long couponId) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 쿠폰 검증
        couponValidator.validateAdminIssuedCouponWithException(coupon);

        // 3. 쿠폰 발급
        couponIssuanceManager.issueInstantCouponToUser(userId, coupon.getId(), TriggerType.ADMIN);
    }

    /**
     * <h3>ADMIN이 사용자들에게 쿠폰 발급해주는 함수</h3>
     */
    public void issueCouponToUsersByAdmin(List<Long> userIds, Long couponId) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 쿠폰 검증
        couponValidator.validateAdminIssuedCouponWithException(coupon);

        // 3. 쿠폰 발급
        couponIssuanceManager.issueInstantCouponToUsers(userIds, coupon.getId(), TriggerType.ADMIN);
    }

    /**
     * <h3>사용자가 쿠폰 코드를 통해 쿠폰을 등록하는 함수</h3>
     */
    public UserCouponOverviewDTO registerCouponByUser(Long userId, String code) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CustomException(CouponErrorType.INVALID_COUPON_FORMAT));

        // 2. 쿠폰 검증
        couponValidator.validateProvisionCouponWithException(userId, coupon);

        // 3. 쿠폰 발급
        UserCoupon userCoupon = couponIssuanceManager.issueInstantCouponToUser(userId, coupon.getId(), TriggerType.REGISTER);

        // 4. DTO 반환
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

    /**
     * <h3>쿠폰 발급과 동시에 다운로드로 처리하는 함수</h3>
     */
    @Transactional
    public UserCouponDownloadDTO issueAndDownloadCoupon(Long userId, Long couponId) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 쿠폰 검증
        couponValidator.validateProvisionCouponWithException(userId, coupon);

        // 3. 쿠폰 발급
        UserCoupon userCoupon = couponIssuanceManager.issueInstantCouponToUser(userId, coupon.getId(), TriggerType.BRAND);

        // 4. 쿠폰 다운로드 처리
        userCoupon.download();

        // 5. DTO 반환
        return UserCouponDownloadDTO.builder()
                .userCouponId(userCoupon.getId())
                .isDownload(userCoupon.getIsDownload())
                .build();
    }

}
