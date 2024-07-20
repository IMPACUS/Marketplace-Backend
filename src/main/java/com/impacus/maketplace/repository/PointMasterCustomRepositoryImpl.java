//package com.impacus.maketplace.repository;
//
//import com.impacus.maketplace.dto.point.response.CurrentPointInfoDTO;
//import com.impacus.maketplace.dto.point.response.QCurrentPointInfoDTO;
//import com.impacus.maketplace.entity.point.QPointHistory;
//import com.impacus.maketplace.entity.point.QPointMaster;
//import com.impacus.maketplace.entity.user.QUser;
//import com.querydsl.core.types.ExpressionUtils;
//import com.querydsl.jpa.JPAExpressions;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//
//@Repository
//@RequiredArgsConstructor
//public class PointMasterCustomRepositoryImpl implements PointMasterCustomRepository{
//
//    private final JPAQueryFactory queryFactory;
//    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
//    private final QPointHistory pointHistoryEntity = QPointHistory.pointHistory;
//
//    @Override
//    public CurrentPointInfoDTO findByUserIdForMyCurrentPointStatus(Long userId) {
//
//        LocalDateTime currentDatetime = LocalDateTime.now();
//        LocalDateTime oneMonthLaterDatetime = currentDatetime.plusMonths(1);
//
//        CurrentPointInfoDTO result = queryFactory.select(new QCurrentPointInfoDTO(
//                        pointMasterEntity.availablePoint,
//                        ExpressionUtils.as(
//                                JPAExpressions.select(pointHistoryEntity.changePoint.sum())
//                                        .from(pointHistoryEntity)
//                                        .where(pointHistoryEntity.expiredAt.goe(currentDatetime)
//                                                .and(pointHistoryEntity.expiredAt.loe(oneMonthLaterDatetime))
//                                                .and(pointMasterEntity.user.id.eq(userId)))
//                                        .groupBy(pointHistoryEntity.pointMasterId)
//                                ,  "scheduledToDisappearPoint")
//                ))
//                .from(pointMasterEntity)
//                .innerJoin(pointHistoryEntity).on(pointHistoryEntity.pointMasterId.eq(pointMasterEntity.id))
//                .where(pointMasterEntity.user.id.eq(userId))
//                .groupBy(pointMasterEntity.id)
//                .fetchOne();
//
//        return result;
//    }
//}
