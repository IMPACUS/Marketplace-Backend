package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.api.QCouponNameDTO;
import com.impacus.maketplace.entity.coupon.QCoupon;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponApiRepositoryImpl implements CouponApiRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QCoupon coupon = QCoupon.coupon;

    @Override
    public List<CouponNameDTO> getCouponNames() {
        return jpaQueryFactory.select(new QCouponNameDTO(
                        coupon.id,
                        coupon.name,
                        coupon.benefitType,
                        coupon.benefitValue
                ))
                .from(coupon)
                .where(coupon.isDeleted.isFalse())
                .fetch();
    }
}
