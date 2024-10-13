package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.product.response.WebProductDTO;
import com.impacus.maketplace.dto.product.response.WebProductTableDTO;
import com.impacus.maketplace.dto.product.response.WebProductTableDetailDTO;
import com.impacus.maketplace.entity.product.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WebProductCustomRepositoryImpl implements WebProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QWishlist wishlist = QWishlist.wishlist;

    @Override
    public CommonProductDTO findCommonProductByProductId(Long productId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(product.id.eq(productId))
                      .and(product.isDeleted.eq(false));

        return queryFactory
                .select(
                        Projections.fields(
                                CommonProductDTO.class,
                                product.id.as("productId"),
                                product.name,
                                product.sellerId,
                                product.productNumber,
                                product.productImages,
                                product.version
                        )
                )
                .from(product)
                .where(booleanBuilder)
                .fetchFirst();
    }

    @Override
    public WebProductDTO findProductByProductId(UserType userType, Long sellerId, Long productId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productId))
            .and(product.isDeleted.eq(false));

        if (userType == UserType.ROLE_APPROVED_SELLER) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
            .and(productOption.isDeleted.eq(false));

        List<WebProductDTO> dtos =
            queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOptionBuilder)
                .leftJoin(wishlist).on(wishlist.productId.eq(product.id))
                .where(productBuilder)
                .groupBy(product.id, productOption.id)
                .transform(
                    GroupBy.groupBy(product.id).list(
                        Projections.constructor(
                            WebProductDTO.class,
                            product.id,
                            product.productNumber,
                            product.name,
                            product.productImages,
                            product.productStatus,
                            wishlist.count(),
                            product.appSalesPrice,
                            product.discountPrice,
                            GroupBy.list(
                                Projections.constructor(
                                    ProductOptionDTO.class,
                                    productOption.id,
                                    productOption.color,
                                    productOption.size
                                )
                            )
                        )
                    )
                );

        return dtos.isEmpty() ? null : dtos.get(0);
    }

    @Override
    public Page<WebProductTableDTO> findProductsForWeb(
        Long sellerId,
        String keyword,
        LocalDate startAt,
        LocalDate endAt,
        Pageable pageable
    ) {
        // 1. builder 생성
        // 1-1. seller 값이 존재하는 경우에만 판매자 비교
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
            .and(product.isDeleted.eq(false))
            .and(product.sellerId.eq(sellerId));

        // 1-2. 검색어 조회
        BooleanBuilder searchBuilder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            searchBuilder.or(product.name.containsIgnoreCase(keyword)); // 검색 옵션: 상품명;
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
            .and(productOption.isDeleted.eq(false));

        // 2. 조건에 맞는 데이터 조회
        List<WebProductTableDTO> dtos = getProductForWebDTO(
            productBuilder, productOptionBuilder, searchBuilder, pageable
        );

        // 3. 전체 데이터 수 조회
        int count = queryFactory
            .selectDistinct(product.id, product.createAt)
            .from(product)
            .leftJoin(productOption).on(productOptionBuilder)
            .where(productBuilder)
            .groupBy(product.id, productOption.color, productOption.size)
            .having(searchBuilder)
            .orderBy(product.createAt.desc())
            .fetch().size();

        // 4. 페이징 처리
        return PaginationUtils.toPage(dtos, pageable, count);
    }

    private List<WebProductTableDTO> getProductForWebDTO(
        BooleanBuilder productBuilder,
        BooleanBuilder productOptionBuilder,
        BooleanBuilder searchBuilder,
        Pageable pageable
    ) {
        // 1. 조건에 맞는 product id 리스트 조회
        // 검색어와 상품 옵션 검색어 확인
        List<Tuple> filteredProductIds = queryFactory
            .selectDistinct(product.id, product.createAt)
            .from(product)
            .leftJoin(productOption).on(productOptionBuilder)
            .where(productBuilder)
            .groupBy(product.id, productOption.color, productOption.size)
            .having(searchBuilder)
            .orderBy(product.createAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<Long> productIds = filteredProductIds.stream().map(x -> x.get(product.id)).toList();

        // 3. productIds 에 포함되는 상품 조회
        JPAQuery<Product> query = queryFactory
            .selectFrom(product)
            .leftJoin(productOption).on(productOptionBuilder)
            .groupBy(product.id, productOption.id)
            .where(product.id.in(productIds))
            .orderBy(product.createAt.desc());
        return query
            .transform(
                GroupBy.groupBy(product.id).list(
                    Projections.constructor(
                        WebProductTableDTO.class,
                        product.id,
                        product.name,
                        product.productImages,
                        product.productNumber,
                        product.deliveryType,
                        product.createAt,
                        GroupBy.list(productOption)
                    )
                )
            );
    }
}
