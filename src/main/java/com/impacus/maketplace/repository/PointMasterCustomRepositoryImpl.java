package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.response.CurrentPointInfoDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.dto.point.response.QCurrentPointInfoDto;
import com.impacus.maketplace.entity.point.QPointHistory;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointMasterCustomRepositoryImpl implements PointMasterCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QPointHistory pointHistoryEntity = QPointHistory.pointHistory;
    private final QUser userEntity = QUser.user;

//    @Override
//    public PointMasterDto findByUserIdForMyInfo(Long userId) {
//        return null;
//    }

    @Override
    public CurrentPointInfoDto findByUserIdForMyCurrentPointStatus(Long userId) {

        LocalDateTime currentDatetime = LocalDateTime.now();
        LocalDateTime oneMonthLaterDatetime = currentDatetime.plusMonths(1);

        CurrentPointInfoDto result = queryFactory.select(new QCurrentPointInfoDto(
                        pointMasterEntity.availablePoint,
                        ExpressionUtils.as(
                                JPAExpressions.select(pointHistoryEntity.changePoint.sum())
                                        .from(pointHistoryEntity)
                                        .where(pointHistoryEntity.expiredAt.goe(currentDatetime)
                                                .and(pointHistoryEntity.expiredAt.loe(oneMonthLaterDatetime))
                                                .and(pointMasterEntity.userId.eq(userId)))
                                        .groupBy(pointHistoryEntity.pointMasterId)
                                ,  "scheduledToDisappearPoint")
                ))
                .from(pointMasterEntity)
                .innerJoin(pointHistoryEntity).on(pointHistoryEntity.pointMasterId.eq(pointMasterEntity.id))
                .where(pointMasterEntity.userId.eq(userId))
                .groupBy(pointMasterEntity.id)
                .fetchOne();

        return result;
    }
}
