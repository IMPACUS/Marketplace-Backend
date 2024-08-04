package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.response.UserCouponDownloadDTO;
import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
import com.impacus.maketplace.entity.coupon.UserCoupon;
import com.impacus.maketplace.repository.coupon.UserCouponRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponUserService {
    private final CouponCustomRepositroy couponCustomRepositroy;
    private final CouponManagementService couponManagementService;
    private final UserCouponRepository userCouponRepository;

    /**
     * 쿠폰함에서 사용자가 가지고 있는 쿠폰 리스트 조회
     * @param userId 사용자 id
     */
    public List<UserCouponOverviewDTO> getUserCouponOverviewList(Long userId) {
        // 1. 사용자가 가지고 있는 쿠폰 조회
        return couponCustomRepositroy.findUserCouponOverviewList(userId);
    }

    /**
     * 사용자 쿠폰 등록하기
     * @param userId 사용자 id
     * @param couponCode 쿠폰 코드
     */
    @Transactional
    public UserCouponOverviewDTO registerUserCoupon(Long userId, String couponCode) {
        return couponManagementService.registerCouponByUser(userId, couponCode);
    }

    @Transactional
    public UserCouponDownloadDTO downloadUserCoupon(Long userId, Long userCouponId) {

        // 1. 쿠폰 가져오기
        UserCoupon userCoupon = userCouponRepository.findByIdAndUserId(userCouponId, userId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_USER_COUPON));

        // 2. 이미 사용한 쿠폰인지 확인
        if (userCoupon.getIsUsed()) {
            throw new CustomException(CouponErrorType.ALREADY_USED_USER_COUPON);
        }

        // 3. 이미 다운로드 받았는지 확인
        if (userCoupon.getIsDownload()) {
            throw new CustomException(CouponErrorType.ALREADY_DOWNLOAD_USER_COUPON);
        }

        // 4. 만료 날짜가 지났는지 확인
        if (userCoupon.getExpiredAt() != null && userCoupon.getExpiredAt().isBefore(LocalDate.now())) {
            throw new CustomException(CouponErrorType.EXPIRED_USER_COUPON);
        }

        // 5. 다운로드 받을 수 있는 날짜 확인
        if (userCoupon.getAvailableDownloadAt().isAfter(LocalDate.now())) {
            throw new CustomException(CouponErrorType.NOT_AVAILABLE_DOWNLOAD_USER_COUPON);
        }

        // 6. 다운로드 처리 및 시간 업데이트
        userCoupon.setIsDownload(true);
        userCoupon.setDownloadAt(LocalDateTime.now());

        return UserCouponDownloadDTO.builder()
                .userCouponId(userCouponId)
                .isDownload(userCoupon.getIsDownload())
                .build();
    }
}
