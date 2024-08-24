package com.impacus.maketplace.repository.product.bundleDelivery.querydsl;

import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupDetailDTO;
import com.impacus.maketplace.dto.bundleDelivery.response.BundleDeliveryGroupProductDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.bundleDelivery.QBundleDeliveryGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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
public class BundleDeliveryGroupCustomRepositoryImpl implements BundleDeliveryGroupCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QBundleDeliveryGroup bundleDeliveryGroup = QBundleDeliveryGroup.bundleDeliveryGroup;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;


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

    @Override
    public Page<BundleDeliveryGroupProductDTO> findProductsByDetailBundleDeliveryGroup(
            Long groupId, String keyword, Pageable pageable
    ) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.isDeleted.eq(false))
                .and(product.bundleDeliveryOption.eq(BundleDeliveryOption.BUNDLE_DELIVERY_AVAILABLE))
                .and(product.bundleDeliveryGroupId.eq(groupId));

        if (keyword != null && !keyword.isEmpty()) {
            productBuilder.and(product.name.containsIgnoreCase(keyword));
        }

        // 조건에 맞는 상품 아이디 조회
        List<Long> productIds = queryFactory
                .select(product.id)
                .from(product)
                .where(productBuilder)
                .orderBy(product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // productIds 조건에 맞는 상품 조회
        List<BundleDeliveryGroupProductDTO> result = queryFactory
                .selectFrom(product)
                .innerJoin(bundleDeliveryGroup).on(bundleDeliveryGroup.id.eq(groupId))
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .where(product.id.in(productIds))
                .orderBy(product.id.asc(), productOption.id.asc())
                .transform(
                        GroupBy.groupBy(product.id)
                                .list(
                                        Projections.constructor(
                                                BundleDeliveryGroupProductDTO.class,
                                                product.id.as("productId"),
                                                product.name,
                                                product.productImages,
                                                bundleDeliveryGroup.deliveryFeeRule,
                                                product.bundleDeliveryOptionAppliedAt,
                                                GroupBy.list(productOption)
                                        )
                                )
                );

        int count = queryFactory
                .select(product.id)
                .from(product)
                .where(productBuilder)
                .fetch().size();

        return PaginationUtils.toPage(result, pageable, count);
    }

    @Override
    public long deleteProductFromBundleGroup(Long sellerId, Long groupId, Long productId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.sellerId.eq(sellerId))
                .and(product.bundleDeliveryGroupId.eq(groupId))
                .and(product.id.eq(productId));

        return queryFactory
                .update(product)
                .set(product.bundleDeliveryGroupId, (Long) null)
                .set(product.bundleDeliveryOption, BundleDeliveryOption.INDIVIDUAL_SHIPPING_ONLY)
                .set(product.bundleDeliveryOptionAppliedAt, (LocalDateTime) null)

                .set(product.modifyAt, LocalDateTime.now())
                .set(product.modifyId, currentAuditor)
                .where(builder)
                .execute();
    }
}
