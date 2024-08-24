package com.impacus.maketplace.repository.product.bundleDelivery.querydsl;

import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDetailDTO;
import com.impacus.maketplace.entity.product.bundleDelivery.QBundleDeliveryGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BundleDeliveryGroupCustomRepositoryImpl implements BundleDeliveryGroupCustomRepository {
    private final JPAQueryFactory queryFactory;

    private final QBundleDeliveryGroup bundleDeliveryGroup = QBundleDeliveryGroup.bundleDeliveryGroup;


    @Override
    public Page<BundleDeliveryGroupDetailDTO> findDetailBundleDeliveryGroups(
            Long sellerId,
            String keyword,
            Pageable pageable,
            String sortBy,
            String direction
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bundleDeliveryGroup.isDeleted.eq(false))
                .and(bundleDeliveryGroup.sellerId.eq(sellerId));

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(bundleDeliveryGroup.name.containsIgnoreCase(keyword));
        }

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, bundleDeliveryGroup.id);

        // 동적 정렬 기준 처리
        if (sortBy != null && sortBy.equals("isUsed")) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;
            orderSpecifier = new OrderSpecifier<>(order, bundleDeliveryGroup.isUsed);
        }

        List<BundleDeliveryGroupDetailDTO> result = queryFactory
                .select(
                        Projections.fields(
                                BundleDeliveryGroupDetailDTO.class,
                                bundleDeliveryGroup.id.as("groupId"),
                                bundleDeliveryGroup.groupNumber,
                                bundleDeliveryGroup.name,
                                bundleDeliveryGroup.deliveryFeeRule,
                                bundleDeliveryGroup.isUsed,
                                bundleDeliveryGroup.createAt,
                                bundleDeliveryGroup.modifyAt
                        )
                )
                .from(bundleDeliveryGroup)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int count = queryFactory
                .select(bundleDeliveryGroup)
                .from(bundleDeliveryGroup)
                .where(builder)
                .fetch().size();

        return PaginationUtils.toPage(result, pageable, count);
    }
}
