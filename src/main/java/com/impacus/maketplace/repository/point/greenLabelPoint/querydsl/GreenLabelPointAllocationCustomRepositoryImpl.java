package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointAllocation;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GreenLabelPointAllocationCustomRepositoryImpl implements GreenLabelPointAllocationCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QGreenLabelPointAllocation greenLabelPointAllocation = QGreenLabelPointAllocation.greenLabelPointAllocation;

    @Override
    public List<NotUsedGreenLabelPointAllocationDTO> findNotUsedGreenLabelPointByUserId(Long userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(greenLabelPointAllocation.userId.eq(userId))
                .and(greenLabelPointAllocation.pointStatus.in(List.of(PointUsageStatus.IN_USE, PointUsageStatus.UNUSED)));

        return queryFactory.select(
                        Projections.fields(
                                NotUsedGreenLabelPointAllocationDTO.class,
                                greenLabelPointAllocation.id,
                                greenLabelPointAllocation.remainPoint,
                                greenLabelPointAllocation.expiredAt,
                                greenLabelPointAllocation.pointStatus
                        )
                )
                .from(greenLabelPointAllocation)
                .where(builder)
                .orderBy(greenLabelPointAllocation.expiredAt.asc())
                .fetch();
    }

    @Override
    public void updateGreenLabelPointAllocationById(Long id, PointUsageStatus pointUsageStatus, Long remainPoint) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
                .update(greenLabelPointAllocation)
                .set(greenLabelPointAllocation.pointStatus, pointUsageStatus)
                .set(greenLabelPointAllocation.remainPoint, remainPoint)
                .set(greenLabelPointAllocation.expiredAt, LocalDateTime.now())

                .set(greenLabelPointAllocation.modifyAt, LocalDateTime.now())
                .set(greenLabelPointAllocation.modifyId, currentAuditor)
                .where(greenLabelPointAllocation.id.eq(id))
                .execute();
    }

}
