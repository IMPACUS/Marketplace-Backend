package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.PointType;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                .innerJoin(userEntity).on(userEntity.id.eq(pointMasterEntity.userId));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(userEntity.id.eq(pointHistorySearchDto.getUserId()));

        List<PointHistoryDto> result = query.where(builder).orderBy(pointHistoryEntity.createAt.desc()).fetch();
        return result;
    }

    /**
     * 오늘 부터 6개월 전의 데이터를 가져오는 로직 수정 중
     */
    @Override
    public List<Long> findAllWithNoUseOrSavePoint(LocalDateTime startDate) {
        List<Long> result = queryFactory.selectDistinct(userEntity.id)
                .from(pointHistoryEntity)
                .join(pointMasterEntity).on(pointMasterEntity.userId.eq(pointHistoryEntity.pointMasterId))
                .join(userEntity).on(userEntity.id.eq(pointMasterEntity.userId))
                .where(pointHistoryEntity.createAt.goe(startDate),
                        pointHistoryEntity.pointType.in(PointType.USE, PointType.SAVE))
                .fetch();

        return result;
    }
}
