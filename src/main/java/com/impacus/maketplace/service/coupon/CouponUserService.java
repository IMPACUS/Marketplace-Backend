package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponUserService {
    private final CouponCustomRepositroy couponCustomRepositroy;
    private final CouponManagementService couponManagementService;

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
    public void registerUserCoupon(Long userId, String couponCode) {
        couponManagementService.registerCouponByUser(userId, couponCode);
    }
}
