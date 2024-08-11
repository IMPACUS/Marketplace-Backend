package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointHistory;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointHistoryRelation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GreenLabelPointHistoryCustomRepositoryImpl implements GreenLabelPointHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    private final QGreenLabelPointHistory history = QGreenLabelPointHistory.greenLabelPointHistory;
    private final QGreenLabelPointHistoryRelation relation = QGreenLabelPointHistoryRelation.greenLabelPointHistoryRelation;
    private final QGreenLabelPointAllocation allocation = QGreenLabelPointAllocation.greenLabelPointAllocation;


    @Override
    public Slice<GreenLabelHistoryDTO> findHistoriesByUserId(Long userId, Pageable pageable) {
        BooleanBuilder relationBuilder = new BooleanBuilder();
        relationBuilder.and(relation.greenLabelPointHistoryId.eq(history.id))
                .and(history.pointStatus.eq(PointStatus.GRANT));

        List<GreenLabelHistoryDTO> dtos = queryFactory
                .select(
                        Projections.constructor(
                                GreenLabelHistoryDTO.class,
                                history.id,
                                history.tradeAmount,
                                history.pointType,
                                history.pointStatus,
                                allocation.expiredAt,
                                history.createAt
                        )
                )
                .from(history)
                .leftJoin(relation).on(relationBuilder)
                .leftJoin(allocation).on(allocation.id.eq(relation.greenLabelPointAllocationId))
                .orderBy(history.createAt.desc())
                .where(history.userId.eq(userId))
                .groupBy(history.id, allocation.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (dtos.size() > pageable.getPageSize()) {
            hasNext = true;
            dtos.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(dtos, pageable, hasNext);
    }
}
