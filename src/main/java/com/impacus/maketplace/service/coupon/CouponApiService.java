package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.repository.coupon.querydsl.CouponApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponApiService {

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
}
