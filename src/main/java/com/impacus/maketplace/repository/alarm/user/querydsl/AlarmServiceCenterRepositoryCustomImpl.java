package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetServiceCenterDto;
import com.impacus.maketplace.entity.alarm.user.enums.ServiceCenterEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmServiceCenter.*;

@RequiredArgsConstructor
public class AlarmServiceCenterRepositoryCustomImpl implements AlarmServiceCenterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetServiceCenterDto findData(ServiceCenterEnum serviceCenterEnum, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetServiceCenterDto.class,
                        alarmServiceCenter.id,
                        alarmServiceCenter.kakao,
                        alarmServiceCenter.email,
                        alarmServiceCenter.msg,
                        alarmServiceCenter.push,
                        alarmServiceCenter.comment1,
                        alarmServiceCenter.comment2
                ))
                .from(alarmServiceCenter)
                .where(alarmServiceCenter.content.eq(serviceCenterEnum).and(alarmServiceCenter.userId.eq(userId)))
                .orderBy(alarmServiceCenter.id.desc())
                .limit(1)
                .fetchOne();
    }
}
