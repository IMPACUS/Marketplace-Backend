package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetOrderDeliveryDto;
import com.impacus.maketplace.entity.alarm.user.enums.OrderDeliveryEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmOrderDelivery.*;

@RequiredArgsConstructor
public class AlarmOrderDeliveryRepositoryCustomImpl implements AlarmOrderDeliveryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetOrderDeliveryDto findData(OrderDeliveryEnum orderDelivery, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetOrderDeliveryDto.class,
                        alarmOrderDelivery.id,
                        alarmOrderDelivery.kakao,
                        alarmOrderDelivery.email,
                        alarmOrderDelivery.msg,
                        alarmOrderDelivery.push,
                        alarmOrderDelivery.comment1,
                        alarmOrderDelivery.comment2
                ))
                .from(alarmOrderDelivery)
                .where(alarmOrderDelivery.content.eq(orderDelivery).and(alarmOrderDelivery.userId.eq(userId)))
                .orderBy(alarmOrderDelivery.id.desc())
                .limit(1)
                .fetchOne();
    }
}
