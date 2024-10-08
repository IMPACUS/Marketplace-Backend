package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.dto.point.AlarmPointDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.AppGreenLabelPointDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPoint;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointAllocation;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GreenLabelPointAllocationCustomRepositoryImpl implements GreenLabelPointAllocationCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QGreenLabelPoint greenLabelPoint = QGreenLabelPoint.greenLabelPoint1;
    private final QGreenLabelPointAllocation allocation = QGreenLabelPointAllocation.greenLabelPointAllocation;
    private final QUser user = QUser.user;

    @Override
    public List<NotUsedGreenLabelPointAllocationDTO> findNotUsedGreenLabelPointByUserId(Long userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(allocation.userId.eq(userId))
                .and(allocation.pointStatus.in(List.of(PointUsageStatus.IN_USE, PointUsageStatus.UNUSED)));

        return queryFactory.select(
                        Projections.fields(
                                NotUsedGreenLabelPointAllocationDTO.class,
                                allocation.id,
                                allocation.remainPoint,
                                allocation.expiredAt,
                                allocation.pointStatus
                        )
                )
                .from(allocation)
                .where(builder)
                .orderBy(allocation.expiredAt.asc())
                .fetch();
    }

    @Override
    public void updateGreenLabelPointAllocationById(Long id, PointUsageStatus pointUsageStatus, Long remainPoint) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
                .update(allocation)
                .set(allocation.pointStatus, pointUsageStatus)
                .set(allocation.remainPoint, remainPoint)
                .set(allocation.expiredAt, LocalDateTime.now())

                .set(allocation.modifyAt, LocalDateTime.now())
                .set(allocation.modifyId, currentAuditor)
                .where(allocation.id.eq(id))
                .execute();
    }

    @Override
    public AppGreenLabelPointDTO findPointInformationByUserId(Long userId) {
        // 1. 그린 라벨 포인트 조회
        Long point = queryFactory
                .select(greenLabelPoint.greenLabelPoint)
                .from(greenLabelPoint)
                .where(greenLabelPoint.userId.eq(userId))
                .fetchFirst();

        // 2. 소멸 예정인 그린 라벨 포인트 조회
        BooleanBuilder allocationBuilder = new BooleanBuilder();
        allocationBuilder.and(allocation.userId.eq(userId))
                .and(allocation.pointStatus.in(List.of(PointUsageStatus.UNUSED, PointUsageStatus.IN_USE)))
                .and(allocation.expiredAt.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
        Long pointsExpiringIn30Days = queryFactory
                .select(allocation.remainPoint.sum())
                .from(allocation)
                .where(allocationBuilder)
                .fetchOne();

        return AppGreenLabelPointDTO.toDTO(point, pointsExpiringIn30Days);
    }

    @Override
    public LocalDateTime findRecentAllocatedPointAtByUserIdAndPointType(Long userId, PointType pointType) {
        BooleanBuilder allocationBuilder = new BooleanBuilder();
        allocationBuilder.and(allocation.userId.eq(userId))
                .and(allocation.pointType.eq(pointType));

        return queryFactory
                .select(allocation.createAt)
                .from(allocation)
                .where(allocationBuilder)
                .orderBy(allocation.createAt.desc())
                .fetchFirst();
    }

    @Override
    public Long findAllocatedPointCntByUserIdAndPointType(Long userId, PointType pointType) {
        BooleanBuilder allocationBuilder = new BooleanBuilder();
        allocationBuilder.and(allocation.userId.eq(userId))
                .and(allocation.pointType.eq(pointType))
                .and(allocation.createAt.between(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX)));

        return queryFactory
                .select(allocation.count())
                .from(allocation)
                .where(allocationBuilder)
                .orderBy(allocation.createAt.desc())
                .fetchOne();
    }

    @Override
    public AlarmPointDTO findAlarmPointByAllocationId(Long greenLabelPointAllocationId) {
        return queryFactory
                .select(
                        Projections.fields(
                                AlarmPointDTO.class,
                                user.name.as("userName"),
                                allocation.remainPoint,
                                allocation.expiredAt
                        )
                )
                .from(allocation)
                .innerJoin(user).on(allocation.userId.eq(user.id))
                .where(allocation.id.eq(greenLabelPointAllocationId))
                .fetchFirst();
    }

}
