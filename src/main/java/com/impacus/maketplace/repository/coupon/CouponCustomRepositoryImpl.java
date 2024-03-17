package com.impacus.maketplace.repository.coupon;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.dto.coupon.response.QCouponUserInfoResponse;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponCustomRepositoryImpl implements CouponCustomRepository{

    private final JPAQueryFactory queryFactory;

    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QUser userEntity = QUser.user;

    @Override
    public CouponUserInfoResponse findByAddCouponInfo(CouponUserInfoRequest request) {

        CouponUserInfoResponse result = queryFactory.select(new QCouponUserInfoResponse(
                        userEntity.name,
                        Expressions.enumPath(UserStatus.class, userEntity.status.toString()),
                        pointMasterEntity.availablePoint,
                        pointMasterEntity.userScore,
                        Expressions.constant("010-000-0000"),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", userEntity.createAt),
                        Expressions.constant("profile-path"),
                        userEntity.email
                ))
                .from(pointMasterEntity)
                .innerJoin(userEntity).on(userEntity.id.eq(pointMasterEntity.userId))
                .where(userEntity.email.eq(request.getUserId()))
                .fetchOne();

        return result;
    }
}
