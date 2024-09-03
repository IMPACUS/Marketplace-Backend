package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetBrandShopDto;
import com.impacus.maketplace.entity.alarm.user.enums.BrandShopEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmBrandShop.alarmBrandShop;

@RequiredArgsConstructor
public class AlarmBrandShopRepositoryCustomImpl implements AlarmBrandShopRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetBrandShopDto findData(BrandShopEnum brandShopEnum, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetBrandShopDto.class,
                        alarmBrandShop.id,
                        alarmBrandShop.kakao,
                        alarmBrandShop.email,
                        alarmBrandShop.msg,
                        alarmBrandShop.push,
                        alarmBrandShop.comment1,
                        alarmBrandShop.comment2
                ))
                .from(alarmBrandShop)
                .where(alarmBrandShop.content.eq(brandShopEnum).and(alarmBrandShop.userId.eq(userId)))
                .orderBy(alarmBrandShop.id.desc())
                .limit(1)
                .fetchOne();
    }
}
