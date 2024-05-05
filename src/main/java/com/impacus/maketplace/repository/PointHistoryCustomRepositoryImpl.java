package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.QPointHistoryDto;
import com.impacus.maketplace.entity.point.QPointHistory;
import com.impacus.maketplace.entity.point.QPointMaster;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryCustomRepositoryImpl implements PointHistoryCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final QPointMaster pointMasterEntity = QPointMaster.pointMaster;
    private final QPointHistory pointHistoryEntity = QPointHistory.pointHistory;
    private final QUser userEntity = QUser.user;

    @Override
    public List<PointHistoryDto> findAllPointHistory(PointHistorySearchDto pointHistorySearchDto) {
        JPAQuery<PointHistoryDto> query = queryFactory.select(new QPointHistoryDto(
                        pointHistoryEntity.pointType,
                        pointHistoryEntity.changePoint,
                        pointHistoryEntity.isManual,
                        pointHistoryEntity.createAt,
                        pointHistoryEntity.expiredAt
                ))
                .from(pointHistoryEntity)
                .innerJoin(pointMasterEntity).on(pointMasterEntity.id.eq(pointHistoryEntity.pointMasterId))
                .innerJoin(userEntity).on(userEntity.id.eq(pointMasterEntity.user.id));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(userEntity.id.eq(pointHistorySearchDto.getUserId()));

        List<PointHistoryDto> result = query.where(builder).orderBy(pointHistoryEntity.createAt.desc()).fetch();
        return result;
    }

//    @Override
//    public List<Long> findAllNoUseUser(LocalDateTime startDate, LocalDateTime endDate) {
//        List<Long> result = queryFactory.selectDistinct(userEntity.id)
//                .from(pointHistoryEntity)
//                .join(pointMasterEntity)
//                    .on(pointMasterEntity.id.eq(pointHistoryEntity.pointMasterId))
//                .join(userEntity)
//                    .on(userEntity.id.eq(pointMasterEntity.userId)
//                      , userEntity.isDormancy.eq(false)
//                      , userEntity.createAt.before(startDate))
//                .where(pointHistoryEntity.pointMasterId.notIn(
//                        JPAExpressions
//                                .selectDistinct(pointHistoryEntity.pointMasterId)
//                                .from(pointHistoryEntity)
//                                .where(pointHistoryEntity.createAt.between(startDate, endDate)
//                                     , pointHistoryEntity.pointType.in(PointType.USE, PointType.SAVE, PointType.JOIN, PointType.CHECK))
//                ))
//                .fetch();
//
//        return result;
//    }
}
