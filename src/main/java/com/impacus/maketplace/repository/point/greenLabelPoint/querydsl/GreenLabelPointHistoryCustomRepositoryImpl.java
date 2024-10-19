package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.WebGreenLabelHistoryDetailDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.QGreenLabelPointHistoryRelation;
import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.QGreenLabelPointHistory;
import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.QOrderGreenLabelPointHistory;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GreenLabelPointHistoryCustomRepositoryImpl implements GreenLabelPointHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    private final QGreenLabelPointHistory history = QGreenLabelPointHistory.greenLabelPointHistory;
    private final QGreenLabelPointHistoryRelation relation = QGreenLabelPointHistoryRelation.greenLabelPointHistoryRelation;
    private final QGreenLabelPointAllocation allocation = QGreenLabelPointAllocation.greenLabelPointAllocation;
    private final QUser user = QUser.user;
    private final QOrderGreenLabelPointHistory orderHistory = QOrderGreenLabelPointHistory.orderGreenLabelPointHistory;


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

    @Override
    public Page<WebGreenLabelHistoryDTO> getGreenLabelPointHistoriesForWeb(
            Pageable pageable,
            String keyword,
            RewardPointStatus status,
            LocalDate startAt,
            LocalDate endAt
    ) {
        // 1. 검색어 필터링
        BooleanBuilder builder = getBooleanBuilderInWebGreenLabelHistoryDTO(
                keyword,
                status,
                startAt,
                endAt
        );

        // 2. 데이터 조회
        List<WebGreenLabelHistoryDTO> dtos = getWebGreenLabelHistoryDTOs(builder, pageable);

        long count = queryFactory
                .select(history.id.count())
                .from(history)
                .innerJoin(user).on(user.id.eq(history.userId))
                .where(builder)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

    public BooleanBuilder getBooleanBuilderInWebGreenLabelHistoryDTO(
            String keyword,
            RewardPointStatus status,
            LocalDate startAt,
            LocalDate endAt
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(history.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .and(history.pointStatus.eq(PointStatus.GRANT));

        if (keyword != null && !keyword.isBlank()) {
            builder.and(user.email.containsIgnoreCase(keyword));
        }
        if (status != null) {
            switch (status) {
                case FAILED, MANUAL, COMPLETED -> {
                    builder.and(PointType.inEnumValue(history.pointType, status));
                }
                default -> {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "status 데이터가 유효하지 않습니다.");
                }
            }
        }

        return builder;
    }

    public List<WebGreenLabelHistoryDTO> getWebGreenLabelHistoryDTOs(
            BooleanBuilder builder,
            Pageable pageable
    ) {
        JPAQuery<WebGreenLabelHistoryDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                WebGreenLabelHistoryDTO.class,
                                history.id,
                                history.pointType,
                                history.tradeAmount,
                                user.id,
                                user.email,
                                user.name,
                                history.createAt
                        )
                )
                .from(history)
                .innerJoin(user).on(user.id.eq(history.userId))
                .where(builder)
                .orderBy(history.createAt.desc());

        if (pageable != null) {
            return query
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
        } else {
            return query.fetch();
        }
    }

    @Override
    public List<WebGreenLabelHistoryDTO> exportGreenLabelPointHistoriesForWeb(
            String keyword,
            RewardPointStatus status,
            LocalDate startAt,
            LocalDate endAt
    ) {
        // 1. 검색어 필터링
        BooleanBuilder builder = getBooleanBuilderInWebGreenLabelHistoryDTO(
                keyword,
                status,
                startAt,
                endAt
        );

        // 2. 데이터 조회
        return getWebGreenLabelHistoryDTOs(builder, null);
    }

    @Override
    public Page<WebGreenLabelHistoryDetailDTO> getGreenLabelPointHistoryDetailsForWeb(Long userId, Pageable pageable) {
        // 1. 검색어 필터링
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(history.userId.eq(userId));

        // 2. 조회
        List<WebGreenLabelHistoryDetailDTO> dtos = queryFactory
                .select(
                        Projections.constructor(
                                WebGreenLabelHistoryDetailDTO.class,
                                history.id,
                                history.greenLabelPoint,
                                history.levelPoint,
                                history.pointType,
                                history.tradeAmount,
                                history.createAt,
                                history.pointStatus,
                                ExpressionUtils.as(
                                        JPAExpressions.select(orderHistory.orderId)
                                                .from(orderHistory)
                                                .where(orderHistory.id.eq(history.id)),
                                        "orderId"
                                )

                        )
                )
                .from(history)
                .where(builder)
                .orderBy(history.createAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long count = queryFactory
                .select(history.id.count())
                .from(history)
                .where(builder)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }
}
