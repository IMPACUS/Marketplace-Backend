package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.product.dto.SearchProductDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.*;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.deliveryCompany.QSellerDeliveryCompany;
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
import org.springframework.data.domain.SliceImpl;
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
    private final QSellerDeliveryCompany sellerDeliveryCompany = QSellerDeliveryCompany.sellerDeliveryCompany;

    @Override
    public Page<WebProductTableDetailDTO> findProductDetailsForWeb(
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
            searchBuilder.or(product.name.containsIgnoreCase(keyword)); // 검색 옵션: 상품명
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        // 2. 조건에 맞는 데이터 조회
        List<WebProductTableDetailDTO> dtos = getProductForWebDTO(
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

    private List<WebProductTableDetailDTO> getProductForWebDTO(
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
                .orderBy(product.createAt.desc(), productOption.id.desc());
        List<WebProductTableDetailDTO> duplicatedProducts = query
                .transform(
                        GroupBy.groupBy(product.id).list(
                                Projections.constructor(
                                        WebProductTableDetailDTO.class,
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

        return removeDuplicatedProductTableDetailsForWeb(duplicatedProducts);
    }

    private List<WebProductTableDetailDTO> removeDuplicatedProductTableDetailsForWeb(List<WebProductTableDetailDTO> products) {
        // Set to keep track of added productOptionIds to avoid duplication
        Set<Long> existingOptionIds = new HashSet<>();

        // Using Stream API to remove duplicates and manage options
        Map<Long, WebProductTableDetailDTO> productMap = products.stream()
                .collect(Collectors.toMap(
                        WebProductTableDetailDTO::getId,
                        Function.identity(),
                        (existing, replacement) -> {
                            // Add options from replacement to existing without duplicates
                            replacement.getOptions().forEach(option -> {
                                if (!existingOptionIds.contains(option.getProductOptionId())) {
                                    existing.getOptions().add(option);
                                    existingOptionIds.add(option.getProductOptionId());
                                }
                            });
                            return existing;
                        }
                ));

        return new ArrayList<>(productMap.values());
    }


    @Override
    public AppProductDetailDTO findProductByProductIdForApp(Long userId, Long productId) {
        BooleanBuilder wishlistBuilder = new BooleanBuilder();
        wishlistBuilder.and(wishlist.registerId.eq(userId.toString()))
                .and(wishlist.productId.eq(product.id));

        List<AppProductDetailDTO> result = queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOption.productId.eq(product.id).and(productOption.isDeleted.eq(false)))
                .leftJoin(wishlist).on(wishlistBuilder)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(sellerDeliveryCompany).on(sellerDeliveryCompany.sellerId.eq(seller.id))
                .leftJoin(productDeliveryTime).on(productDeliveryTime.productId.eq(product.id))
                .where(product.id.eq(productId))
                .transform(GroupBy.groupBy(product.id).list(
                                Projections.constructor(
                                        AppProductDetailDTO.class,
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
                                        product.productImages,
                                        product.sellerId,
                                        product.deliveryFeeType,
                                        sellerDeliveryCompany.generalDeliveryFee
                                )
                        )
                );

        if (result.isEmpty()) {
            throw new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT);
        }

        AppProductDetailDTO productDTO = result.get(0);

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
    public Slice<AppProductDTO> findProductsByProductIds(
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
        List<AppProductDTO> products = findProducts(userId, productBuilder);

        // 2. 최근 조회한 상품 순으로 정렬
        List<AppProductDTO> sortedProducts = products.stream()
                .sorted(Comparator.comparingInt(product -> productIdIndexMap.get(product.getProductId())))
                .toList();

        return PaginationUtils.toSlice(sortedProducts, pageable);
    }

    @Override
    public WebProductDetailDTO findProductDetailByProductId(Long sellerId, UserType userType, Long productId) {
        // BooleanBuilder 생성
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productId))
                .and(product.isDeleted.eq(false));
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        // 조회
        List<WebProductDetailDTO> duplicatedProducts =
                queryFactory
                        .selectFrom(product)
                        .leftJoin(productDetailInfo).on(productDetailInfo.productId.eq(product.id))
                        .leftJoin(productDeliveryTime).on(productDeliveryTime.productId.eq(product.id))
                        .leftJoin(productOption).on(productOptionBuilder)
                        .leftJoin(productClaimInfo).on(productClaimInfo.productId.eq(product.id))
                        .where(productBuilder)
                        .transform(GroupBy.groupBy(product.id).list(
                                Projections.fields(
                                        WebProductDetailDTO.class,
                                        product.id.as("productId"),
                                        product.productNumber,
                                        product.version,
                                        Projections.fields(
                                                WebProductBasicDTO.class,
                                                product.name,
                                                product.deliveryType,
                                                product.isCustomProduct,
                                                product.categoryId,
                                                product.deliveryFeeType,
                                                product.refundFeeType,
                                                product.deliveryFee,
                                                product.refundFee,
                                                product.specialDeliveryFee,
                                                product.specialRefundFee,
                                                product.deliveryCompany,
                                                product.bundleDeliveryOption,
                                                product.bundleDeliveryGroupId,
                                                product.productImages,
                                                product.marketPrice,
                                                product.appSalesPrice,
                                                product.discountPrice,
                                                product.salesChargePercent,
                                                Projections.constructor(
                                                        ProductDeliveryTimeDTO.class,
                                                        productDeliveryTime.minDays,
                                                        productDeliveryTime.maxDays
                                                ).as("deliveryTime")
                                        ).as("information"),
                                        Projections.fields(
                                                WebProductSpecificationDTO.class,
                                                product.description,
                                                product.weight,
                                                product.productStatus,
                                                product.type
                                        ).as("specification"),
                                        GroupBy.set(
                                                Projections.constructor(
                                                        ProductOptionDTO.class,
                                                        productOption.id,
                                                        productOption.color,
                                                        productOption.size
                                                )
                                        ).as("productOptions"),
                                        Projections.constructor(
                                                ProductDetailInfoDTO.class,
                                                productDetailInfo
                                        ).as("productDetail"),
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

        // 중복된 상품 옵션 제거
        List<WebProductDetailDTO> products = removeDuplicatedProductDetailsForWeb(duplicatedProducts);

        return products.isEmpty() ? null : products.get(0);
    }

    private List<WebProductDetailDTO> removeDuplicatedProductDetailsForWeb(List<WebProductDetailDTO> products) {
        // Using Stream API to remove duplicates
        Map<Long, WebProductDetailDTO> productMap = products.stream()
                .collect(Collectors.toMap(
                        WebProductDetailDTO::getProductId,
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
    public Slice<AppProductDTO> findProductsByName(Long userId, String name, Pageable pageable) {
        // where 조건 생성
        BooleanBuilder productBuilder = new BooleanBuilder()
                .and(product.name.containsIgnoreCase(name));

        // 조건에 맞는 productId 리스트 조회
        List<Long> productIds = queryFactory
                .select(product.id)
                .from(product)
                .where(productBuilder)
                .orderBy(product.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 상품 조회
        List<AppProductDTO> dtos = findProducts(
                userId,
                new BooleanBuilder().and(product.id.in(productIds))
        );

        // Slice 생성
        return PaginationUtils.toSlice(dtos, pageable);
    }

    @Override
    public Slice<SearchProductDTO> findAllBy(Pageable pageable) {
        // 데이터 조회
        List<SearchProductDTO> content = queryFactory
                .select(Projections.fields(
                        SearchProductDTO.class,
                        product.id.as("productId"),
                        product.name
                ))
                .from(product)
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize() + 1) // 추가 데이터로 다음 페이지 여부 판단
                .fetch();

        // 다음 페이지 여부 체크
        boolean hasNext = content.size() > pageable.getPageSize();

        // 추가 데이터 제거
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<AppProductDTO> findAllProductBySubCategoryId(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    ) {
        // 1. 상품 리스트 조회
        List<AppProductDTO> dtos = findProductsForApp(userId, subCategoryId, pageable);

        // 3. 슬라이스 처리
        return PaginationUtils.toSlice(dtos, pageable);
    }

    private List<AppProductDTO> findProductsForApp(
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


    private List<AppProductDTO> findProducts(
            Long userId,
            BooleanBuilder productBuilder
    ) {
        BooleanBuilder wishlistBuilder = new BooleanBuilder()
                .and(wishlist.productId.eq(product.id));
        if (userId != null) {
            wishlistBuilder.and(wishlist.registerId.eq(userId.toString()));
        }

        return queryFactory
                .selectFrom(product)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(sellerDeliveryCompany).on(sellerDeliveryCompany.sellerId.eq(seller.id))
                .leftJoin(wishlist).on(wishlistBuilder)
                .where(productBuilder)
                .transform(
                        GroupBy.groupBy(product.id).list(Projections.constructor(
                                AppProductDTO.class,
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
                                product.createAt,
                                product.deliveryFeeType,
                                sellerDeliveryCompany.generalDeliveryFee

                                )
                        )
                );
    }
}
