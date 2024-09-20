package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.*;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QSubCategory subCategory = QSubCategory.subCategory;
    private final QSeller seller = QSeller.seller;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProductDeliveryTime productDeliveryTime = QProductDeliveryTime.productDeliveryTime;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;
    private final QProductClaimInfo productClaimInfo = QProductClaimInfo.productClaimInfo;

    @Override
    public Page<ProductForWebDTO> findProductsForWeb(
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
                .and(product.isDeleted.eq(false));
        if (sellerId != null) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }
        // 1-2. 검색어 조회
        BooleanBuilder searchBuilder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            searchBuilder.or(product.name.containsIgnoreCase(keyword)) // 검색 옵션: 상품명
                    .or(product.productNumber.containsIgnoreCase(keyword)) // 검색 옵션: 상품 번호
                    .or(DeliveryType.containsEnumValue(product.deliveryType, keyword)) // 검색 옵션: 배송 상태
                    .or(ProductStatus.containsEnumValue(product.productStatus, keyword)) // 검색 옵션: 상품 상태
                    .or(Expressions.stringTemplate("cast({0} as string)", product.appSalesPrice).contains(keyword))  // 검색 옵션: 할인가
                    .or(Expressions.stringTemplate("CAST(SUM({0}) AS string)", productOption.stock)
                            .like("%" + keyword + "%")) // 검색 옵션: 재고
                    .or(Expressions.stringTemplate("concat({0}, '/', {1})", productOption.color, productOption.size) // 검색 옵션: 상품 옵션
                            .containsIgnoreCase(keyword));
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        // 2. 조건에 맞는 데이터 조회
        List<ProductForWebDTO> dtos = getProductForWebDTO(
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

    private List<ProductForWebDTO> getProductForWebDTO(
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
                                        ProductForWebDTO.class,
                                        product.id,
                                        product.name,
                                        Expressions.stringTemplate("cast({0} as string)", product.appSalesPrice),
                                        product.productNumber,
                                        product.deliveryType,
                                        product.productStatus,
                                        product.createAt,
                                        GroupBy.list(productOption),
                                        product.productImages
                                )
                        )
                );
    }

    @Override
    public DetailedProductDTO findProductByProductId(Long userId, Long productId) {
        BooleanBuilder wishlistBuilder = new BooleanBuilder();
        wishlistBuilder.and(wishlist.registerId.eq(userId.toString()))
                .and(wishlist.productId.eq(product.id));

        List<DetailedProductDTO> result = queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .leftJoin(wishlist).on(wishlistBuilder)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(productDeliveryTime).on(productDeliveryTime.productId.eq(product.id))
                .where(product.id.eq(productId))
                .transform(GroupBy.groupBy(product.id).list(
                                Projections.constructor(
                                        DetailedProductDTO.class,
                                        product.id,
                                        product.name,
                                        product.appSalesPrice,
                                        product.discountPrice,
                                        product.type,
                                        GroupBy.list(productOption),
                                        wishlist.id,
                                        product.deliveryFee,
                                        seller.marketName,
                                        product.description,
                                        Projections.constructor(
                                                ProductDeliveryTimeDTO.class,
                                                productDeliveryTime.minDays,
                                                productDeliveryTime.maxDays
                                        ),
                                        product.productImages
                                )
                        )
                );

        if (result.isEmpty()) {
            throw new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT);
        }

        DetailedProductDTO productDTO = result.get(0);

        Long wishlistCnt = queryFactory.select(count(wishlist))
                .from(wishlist)
                .where(wishlist.productId.eq(productId))
                .fetchOne();

        productDTO.setWishlistCnt(wishlistCnt != null ? wishlistCnt : 0L);

        return productDTO;
    }

    @Override
    public boolean existsBySuperCategoryId(Long superCategoryId) {
        Long productCnt = queryFactory
                .select(count(product))
                .from(product)
                .innerJoin(subCategory).on(subCategory.superCategoryId.eq(superCategoryId))
                .where(product.categoryId.eq(subCategory.id))
                .fetchFirst();

        return productCnt > 0;
    }

    @Override
    public Slice<ProductForAppDTO> findProductsByProductIds(
            Long userId,
            List<Long> productIds,
            Pageable pageable
    ) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder
                .and(product.id.in(productIds))
                .and(product.isDeleted.eq(false));

        Map<Long, Integer> productIdIndexMap = new HashMap<>();
        for (int i = 0; i < productIds.size(); i++) {
            productIdIndexMap.put(productIds.get(i), i);
        }

        // 1. 조건에 맞는 Product 조회
        List<ProductForAppDTO> products = findProducts(userId, productBuilder);

        // 2. 최근 조회한 상품 순으로 정렬
        List<ProductForAppDTO> sortedProducts = products.stream()
                .sorted(Comparator.comparingInt(product -> productIdIndexMap.get(product.getProductId())))
                .toList();

        return PaginationUtils.toSlice(sortedProducts, pageable);
    }

    @Override
    public ProductDetailForWebDTO findProductDetailByProductId(Long sellerId, UserType userType, Long productId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productId))
                .and(product.isDeleted.eq(false));
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        List<ProductDetailForWebDTO> duplicatedProducts =
                queryFactory
                        .selectFrom(product)
                        .leftJoin(productDetailInfo).on(productDetailInfo.productId.eq(product.id))
                        .leftJoin(productDeliveryTime).on(productDeliveryTime.productId.eq(product.id))
                        .leftJoin(productOption).on(productOptionBuilder)
                        .leftJoin(productClaimInfo).on(productClaimInfo.productId.eq(product.id))
                        .where(productBuilder)
                        .transform(GroupBy.groupBy(product.id).list(Projections.fields(
                                        ProductDetailForWebDTO.class,
                                        product.id,
                                        product.name,
                                        product.categoryId,
                                        product.deliveryType,
                                        product.isCustomProduct,
                                        product.deliveryFeeType,
                                        product.refundFeeType,
                                        product.deliveryFee,
                                        product.refundFee,
                                        product.specialDeliveryFee,
                                        product.specialRefundFee,
                                        product.deliveryCompany,
                                        product.bundleDeliveryOption,
                                        product.bundleDeliveryGroupId,
                                        product.salesChargePercent,
                                        product.marketPrice,
                                        product.appSalesPrice,
                                        product.discountPrice,
                                        product.weight,
                                        product.type,
                                        product.productStatus,
                                        product.description,
                                        Projections.constructor(
                                                ProductDetailInfoDTO.class,
                                                productDetailInfo
                                        ).as("productDetail"),
                                        Projections.constructor(
                                                ProductDeliveryTimeDTO.class,
                                                productDeliveryTime.minDays,
                                                productDeliveryTime.maxDays
                                        ).as("deliveryTime"),
                                        GroupBy.set(
                                                Projections.constructor(
                                                        ProductOptionDTO.class,
                                                        productOption.id,
                                                        productOption.color,
                                                        productOption.size
                                                )
                                        ).as("productOptions"),
                                        product.productImages,
                                        Projections.constructor(
                                                ProductClaimInfoDTO.class,
                                                productClaimInfo.recallInfo,
                                                productClaimInfo.claimCost,
                                                productClaimInfo.claimPolicyGuild,
                                                productClaimInfo.claimContactInfo
                                        ).as("claim")
                                        )
                                )
                        );

        List<ProductDetailForWebDTO> products = removeDuplicatedProductDetailsForWeb(duplicatedProducts);

        return products.isEmpty() ? null : products.get(0);
    }

    private List<ProductDetailForWebDTO> removeDuplicatedProductDetailsForWeb(List<ProductDetailForWebDTO> products) {
        // Using Stream API to remove duplicates
        Map<Long, ProductDetailForWebDTO> productMap = products.stream()
                .collect(Collectors.toMap(
                        ProductDetailForWebDTO::getId,
                        Function.identity(),
                        (existing, replacement) -> {
                            existing.getProductOptions().addAll(replacement.getProductOptions());
                            return existing;
                        }
                ));

        return new ArrayList<>(productMap.values());
    }

    @Override
    public boolean checkIsSellerProductIds(Long userId, List<Long> productIds) {
        // 1. productIds에 존재하는 userId가 등록한 판매자 id 조회
        List<Long> sellerProductIds = queryFactory
                .select(product.id)
                .from(product)
                .leftJoin(seller).on(seller.userId.eq(userId))
                .where(product.sellerId.eq(seller.id))
                .where(product.id.in(productIds))
                .fetch();

        // 2. 판매자가 등록한 상품들인지 확인
        // sellerProductIds.size()와 productIds.size()가 다르면 판매자가 등록하지 않는 상품이 존재하는 것으로 판단
        return sellerProductIds.size() == productIds.size();
    }

    @Override
    public Slice<ProductForAppDTO> findAllProductBySubCategoryId(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    ) {
        // 1. 상품 리스트 조회
        List<ProductForAppDTO> dtos = findProductsForApp(userId, subCategoryId, pageable);

        // 3. 슬라이스 처리
        return PaginationUtils.toSlice(dtos, pageable);
    }

    private List<ProductForAppDTO> findProductsForApp(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    ) {
        BooleanBuilder productBuilder = new BooleanBuilder()
                .and(product.isDeleted.eq(false));
        // 카테고리가 검색에 존재할 때만 검색
        if (subCategoryId != null) {
            productBuilder
                    .and(product.categoryId.eq(subCategoryId));
        }

        // 1. 조건에 맞는 productId 리스트 조회
        List<Long> productIds = queryFactory
                .select(product.id)
                .from(product)
                .where(productBuilder)
                .orderBy(product.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 2. productIds 조건에 맞는 Product 리스트 조회
        return findProducts(
                userId,
                new BooleanBuilder().and(product.id.in(productIds))
        );
    }


    private List<ProductForAppDTO> findProducts(
            Long userId,
            BooleanBuilder productBuilder
    ) {
        BooleanBuilder wishlistBuilder = new BooleanBuilder();
        wishlistBuilder.and(wishlist.registerId.eq(userId.toString()))
                .and(wishlist.productId.eq(product.id));

        return queryFactory
                .selectFrom(product)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(wishlist).on(wishlistBuilder)
                .where(productBuilder)
                .transform(
                        GroupBy.groupBy(product.id).list(Projections.constructor(
                                ProductForAppDTO.class,
                                product.id,
                                        product.name,
                                        seller.marketName,
                                        product.appSalesPrice,
                                        product.deliveryType,
                                        product.discountPrice,
                                product.productImages,
                                        wishlist.id,
                                        product.deliveryFee,
                                        product.type,
                                        product.createAt

                                )
                        )
                );
    }
}
