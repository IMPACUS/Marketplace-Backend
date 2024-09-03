package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetShoppingBenefitsDto;
import com.impacus.maketplace.entity.alarm.user.enums.ShoppingBenefitsEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmShoppingBenefits.*;

@RequiredArgsConstructor
public class AlarmShoppingBenefitsRepositoryCustomImpl implements AlarmShoppingBenefitsRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetShoppingBenefitsDto findData(ShoppingBenefitsEnum shoppingBenefitsEnum, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetShoppingBenefitsDto.class,
                        alarmShoppingBenefits.id,
                        alarmShoppingBenefits.kakao,
                        alarmShoppingBenefits.email,
                        alarmShoppingBenefits.msg,
                        alarmShoppingBenefits.push,
                        alarmShoppingBenefits.comment1,
                        alarmShoppingBenefits.comment2
                ))
                .from(alarmShoppingBenefits)
                .where(alarmShoppingBenefits.content.eq(shoppingBenefitsEnum).and(alarmShoppingBenefits.userId.eq(userId)))
                .orderBy(alarmShoppingBenefits.id.desc())
                .limit(1)
                .fetchOne();
    }
}
