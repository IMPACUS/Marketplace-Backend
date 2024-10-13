package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.repository.coupon.querydsl.CouponApiRepository;
import com.impacus.maketplace.service.api.CouponApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponApiServiceImpl implements CouponApiService {

    private final CouponApiRepository couponApiRepository;
    private final CouponIssuanceService couponIssuanceService;

    /**
     * 쿠폰 이름 및 금액 정보 가져오기
     * 조건: 삭제 X
     */
    public List<CouponNameDTO> getCouponNames() {
        return couponApiRepository.getCouponNames();
    }

    @Transactional
    public void issueCouponUser(Long userId, Long couponId) {
        couponIssuanceService.issueCouponTargetUserByAdmin(couponId, userId);
    }

    public Map<Long, List<AlarmCouponDTO>> getAlarmCoupon() {
        List<AlarmCouponDTO> alarmCoupons = couponApiRepository.getAlarmCoupons();

        Map<Long, List<AlarmCouponDTO>> alarmCouponsMap = new HashMap<>();

        alarmCoupons.forEach(alarmCouponDTO ->
                alarmCouponsMap.computeIfAbsent(alarmCouponDTO.getUserId(), k -> new ArrayList<>())
        );

        return alarmCouponsMap;
    }
}
