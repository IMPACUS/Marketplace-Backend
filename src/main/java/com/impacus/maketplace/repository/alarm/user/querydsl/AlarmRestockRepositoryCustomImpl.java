package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetRestockDto;
import com.impacus.maketplace.entity.alarm.user.enums.RestockEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmRestock.*;

@RequiredArgsConstructor
public class AlarmRestockRepositoryCustomImpl implements AlarmRestockRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetRestockDto findData(RestockEnum restockEnum, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetRestockDto.class,
                        alarmRestock.id,
                        alarmRestock.kakao,
                        alarmRestock.email,
                        alarmRestock.msg,
                        alarmRestock.push,
                        alarmRestock.comment1,
                        alarmRestock.comment2
                ))
                .from(alarmRestock)
                .where(alarmRestock.content.eq(restockEnum).and(alarmRestock.userId.eq(userId)))
                .orderBy(alarmRestock.id.desc())
                .limit(1)
                .fetchOne();
    }
}
