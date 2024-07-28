package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자가 쿠폰을 사용하고, 시스템이 쿠폰을 지급하는 로직이 담겨있는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponRedemptionService {

    private final CouponRepository couponRepository;

    @Transactional
    public void issueCouponTargetUserByAdmin(Long couponId, User user) {

        // 1. 등록되어 있는 쿠폰 조회

        // 2. 쿠폰 발급하기

        // 3. 발급 이력 기록하기

    }

    @Transactional
    public void issueCouponAllUserByAdmin(Long couponId, UserLevel userLevel) {

        // 1. 등록되어 있는 쿠폰 조회

        // 2.1 레벨이 있을 경우 레벨에 맞는 유저 조회
        // 2.2 레벨이 null일 경우 모든 유저 조회

        // 3. 삭제되지 않은 유저에 한해서 쿠폰 발급

        // 4. 발급 이력 기록하기
    }
}
