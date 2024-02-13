package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponIssuedDto;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.CouponRepository;
import com.impacus.maketplace.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    public static final String COUPON_CODE = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public Boolean addCoupon(CouponIssuedDto couponIssuedDto) {
        Coupon coupon = Coupon.builder()
                .code(CouponUtils.getUUIDCouponCode())
                .expiredAt(stringToLocalDateTime(couponIssuedDto.getExpiredAt()))
                .discount(couponIssuedDto.getDiscount())
                .couponType(couponIssuedDto.getCouponTypeEnum())
                .constraints(couponIssuedDto.getConstraints())
                .build();
        // TODO: 쿠폰 저장 해야할 차례 02-13
        return null;
    }

    private LocalDateTime stringToLocalDateTime(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, dtf);
    }





}
