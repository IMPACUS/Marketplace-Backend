package com.impacus.maketplace.repository.alarm.user.querydsl;

import com.impacus.maketplace.dto.alarm.user.get.GetReviewDto;
import com.impacus.maketplace.entity.alarm.user.enums.ReviewEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.impacus.maketplace.entity.alarm.user.QAlarmReview.*;

@RequiredArgsConstructor
public class AlarmReviewRepositoryCustomImpl implements AlarmReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GetReviewDto findData(ReviewEnum reviewEnum, Long userId) {
        return jpaQueryFactory.select(Projections.constructor(
                        GetReviewDto.class,
                        alarmReview.id,
                        alarmReview.kakao,
                        alarmReview.email,
                        alarmReview.msg,
                        alarmReview.push,
                        alarmReview.comment1,
                        alarmReview.comment2
                ))
                .from(alarmReview)
                .where(alarmReview.content.eq(reviewEnum).and(alarmReview.userId.eq(userId)))
                .orderBy(alarmReview.id.desc())
                .limit(1)
                .fetchOne();
    }
}
