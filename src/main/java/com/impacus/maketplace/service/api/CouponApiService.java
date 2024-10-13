package com.impacus.maketplace.service.api;

import com.impacus.maketplace.dto.coupon.api.AlarmCouponDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;

import java.util.List;
import java.util.Map;

/**
 * 쿠폰 Api 요청 서비스
 */
public interface CouponApiService {

    /**
     * 쿠폰 이름 및 금액 정보 가져오기
     * 조건: 삭제 X
     */
    List<CouponNameDTO> getCouponNames();

    /**
     * 어드민이 무조건적으로 쿠폰 발급해주는 서비스
     * 조건: 삭제되지 않은 쿠폰
     */
    void issueCouponUser(Long userId, Long couponId);

    /**
     * 알람 보내야 되는 사용자 쿠폰 정보 가져오기
     * @return {Map<UesrId, List<AlarmCouponDTO>>}
     */
    Map<Long, List<AlarmCouponDTO>> getAlarmCoupon();
}
