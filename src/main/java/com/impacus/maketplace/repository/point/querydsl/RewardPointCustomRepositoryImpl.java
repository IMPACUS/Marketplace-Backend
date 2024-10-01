package com.impacus.maketplace.repository.point.querydsl;

import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.point.RewardPointDTO;
import com.impacus.maketplace.entity.point.QRewardPoint;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RewardPointCustomRepositoryImpl implements RewardPointCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QRewardPoint rewardPoint = QRewardPoint.rewardPoint;

    @Override
    public Page<RewardPointDTO> getRewardPoints(Pageable pageable, String keyword, RewardPointStatus status) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(rewardPoint.isDeleted.isFalse());

        // 1. 검색어 필터링
        if (keyword != null && !keyword.isBlank()) {
            builder.and(RewardPointType.containsEnumValue(rewardPoint.rewardPointType, keyword));
        }

        // 2. 정렬 기준 생성
        NumberExpression<Integer> statusPriority = null;
        if (status != null) {
            statusPriority =
                    Expressions.cases()
                            .when(rewardPoint.status.eq(status)).then(1)
                            .otherwise(2);
        }

        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, rewardPoint.createAt);

        List<RewardPointDTO> dtos = queryFactory
                .select(
                        Projections.constructor(
                                RewardPointDTO.class,
                                rewardPoint.id,
                                rewardPoint.rewardPointType,
                                rewardPoint.expirationPeriod,
                                rewardPoint.issueQuantity,
                                rewardPoint.grantMethod,
                                rewardPoint.status,
                                rewardPoint.createAt
                        )
                )
                .from(rewardPoint)
                .where(builder)
                .orderBy(statusPriority != null ? statusPriority.asc() : rewardPoint.createAt.desc(), orderSpecifier)
                .fetch();

        long count = queryFactory
                .select(rewardPoint.id.count())
                .from(rewardPoint)
                .where(builder)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

    @Override
    public long updateRewardPointStatus(List<Long> rewardPointIds, RewardPointStatus status) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        return queryFactory
                .update(rewardPoint)
                .set(rewardPoint.status, status)
                .set(rewardPoint.modifyAt, LocalDateTime.now())
                .set(rewardPoint.modifyId, currentAuditor)
                .where(rewardPoint.id.in(rewardPointIds)
                        .and(rewardPoint.status.ne(RewardPointStatus.COMPLETED)))
                .execute();
    }
}
