package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.common.QAttachFileGroup;
import com.impacus.maketplace.entity.product.*;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
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
    private final QProductDescription description = QProductDescription.productDescription;
    private final QSubCategory subCategory = QSubCategory.subCategory;
    private final QSeller seller = QSeller.seller;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QAttachFileGroup attachFileGroup = QAttachFileGroup.attachFileGroup;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProductDeliveryTime productDeliveryTime = QProductDeliveryTime.productDeliveryTime;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;
    private final QProductDescription productDescription = QProductDescription.productDescription;
    private final QProductClaimInfo productClaimInfo = QProductClaimInfo.productClaimInfo;

    @Override
    public Page<ProductForWebDTO> findAllProduct(
            Long sellerId,
            String keyword,
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable
    ) {
        // 1. builder 생성
        // - seller 값이 존재하는 경우에만 판매자 비교
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))

                .and(product.isDeleted.eq(false));

        if (sellerId != null) {
            builder.and(product.sellerId.eq(sellerId));
        }

        // 2. 전체 데이터 조회
        List<ProductForWebDTO> duplicatedProducts = getProductDTO(builder);

        // 3. 데이터 중복 제거 (set 사용하는 경우 중복 제거 필요)
        List<ProductForWebDTO> products = removeDuplicatedProductsForWeb(duplicatedProducts);

        // 4. 검색어 조회
        if (keyword != null && !keyword.isBlank()) {
            products = filterProductForWebDTOByKeyword(keyword, products);
        }

        // 5. 페이징 처리
        long count = products.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());
        List<ProductForWebDTO> paginatedProducts = count < start ? new ArrayList<>() : products.subList(start, end);

        return new PageImpl<>(paginatedProducts, pageable, count);
    }

    private List<ProductForWebDTO> removeDuplicatedProductsForWeb(List<ProductForWebDTO> products) {
        // Using Stream API to remove duplicates
        Map<Long, ProductForWebDTO> productMap = products.stream()
                .collect(Collectors.toMap(
                        ProductForWebDTO::getId,
                        Function.identity(),
                        (existing, replacement) -> {
                            existing.getOptions().addAll(replacement.getOptions());
                            existing.getProductImageList().addAll(replacement.getProductImageList());
                            return existing;
                        }
                ));

        return new ArrayList<>(productMap.values());
    }

    private List<ProductForWebDTO> filterProductForWebDTOByKeyword(String keyword, List<ProductForWebDTO> products) {
        List<ProductForWebDTO> content = new ArrayList<>();
        for (ProductForWebDTO dto : products) {
            if (StringUtils.containsKeywordIgnoreCase(dto.getName(), keyword)) { // 검색 옵션: 상품명
                content.add(dto);
            } else if (StringUtils.containsKeywordIgnoreCase(dto.getProductNumber(), keyword)) { // 검색 옵션: 상품 번호
                content.add(dto);
            } else if (StringUtils.containsKeywordIgnoreCase(dto.getDeliveryType().getValue(), keyword)) { // 검색 옵션: 배송 상태
                content.add(dto);
            } else if (StringUtils.containsKeywordIgnoreCase(dto.getProductStatus().getValue(), keyword)) { // 검색 옵션: 상품 상태
                content.add(dto);
            } else if (StringUtils.containsKeywordIgnoreCase(Integer.toString(dto.getPrice()), keyword)) { // 검색 옵션: 할인가
                content.add(dto);
            } else if (StringUtils.containsKeywordIgnoreCase(Long.toString(dto.getStock()), keyword)) { // 검색 옵션: 재고
                content.add(dto);
            } else {
                for (ProductOptionDTO productOptionDTO : dto.getOptions()) { // 검색 옵션: 상품 옵션
                    if (StringUtils.containsKeywordIgnoreCase(productOptionDTO.getColor(), keyword)
                            || StringUtils.containsKeywordIgnoreCase(productOptionDTO.getSize(), keyword)) {
                        content.add(dto);
                        break;
                    }
                }
            }
        }

        return content;
    }


    private List<ProductForWebDTO> getProductDTO(BooleanBuilder builder) {
        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                .leftJoin(productOption).on(productOptionBuilder)
                .groupBy(product.id, attachFile.id, productOption.id)
                .where(builder);
        return query
                .transform(
                        GroupBy.groupBy(product.id).list(
                                Projections.constructor(
                                        ProductForWebDTO.class,
                                        product.id,
                                        product.name,
                                        product.appSalesPrice,
                                        product.productNumber,
                                        product.deliveryType,
                                        product.productStatus,
                                        productOption.stock.sum(),
                                        product.createAt,
                                        GroupBy.set(
                                                Projections.constructor(
                                                        ProductOptionDTO.class,
                                                        productOption.id,
                                                        productOption.color,
                                                        productOption.size
                                                )

                                        ),
                                        GroupBy.set(
                                                Projections.constructor(
                                                        AttachFileDTO.class,
                                                        attachFile.id,
                                                        attachFile.attachFileName
                                                )
                                        )
                                )
                        )
                );
    }

    @Override
    public DetailedProductDTO findProductByProductId(Long userId, Long productId) {
        BooleanBuilder wishlistBuilder = new BooleanBuilder();
        wishlistBuilder.and(wishlist.registerId.eq(userId.toString()))
                .and(wishlist.productId.eq(product.id));

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(description.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        List<DetailedProductDTO> result = queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .leftJoin(description).on(productOptionBuilder)
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
                                        description.description,
                                        Projections.constructor(
                                                ProductDeliveryTimeDTO.class,
                                                productDeliveryTime.minDays,
                                                productDeliveryTime.maxDays
                                        )
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
    public Slice<ProductForAppDTO> findAllProductByProductIds(
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

        List<ProductForAppDTO> products = findAllProduct(productBuilder, userId, pageable);

        List<ProductForAppDTO> sortedProducts = products.stream()
                .sorted(Comparator.comparingInt(product -> productIdIndexMap.get(product.getProductId())))
                .collect(Collectors.toList());

        boolean hasNext = false;
        if (sortedProducts.size() > pageable.getPageSize()) {
            hasNext = true;
            sortedProducts.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(sortedProducts, pageable, hasNext);
    }

    @Override
    public ProductDetailForWebDTO findProductDetailByProductId(Long sellerId, UserType userType, Long productId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productId))
                .and(product.isDeleted.eq(false));
        if (userType == UserType.ROLE_APPROVED_SELLER) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }

        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
                .and(productOption.isDeleted.eq(false));

        List<ProductDetailForWebDTO> duplicatedProducts =
                queryFactory
                        .selectFrom(product)
                        .leftJoin(productDetailInfo).on(productDetailInfo.productId.eq(product.id))
                        .leftJoin(productDescription).on(productDescription.productId.eq(product.id))
                        .leftJoin(productDeliveryTime).on(productDeliveryTime.productId.eq(product.id))
                        .leftJoin(productOption).on(productOptionBuilder)
                        .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                        .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                        .leftJoin(productClaimInfo).on(productClaimInfo.productId.eq(product.id))
                        .where(productBuilder)
                        .transform(GroupBy.groupBy(product.id).list(Projections.fields(
                                                ProductDetailForWebDTO.class,
                                        product.id,
                                                product.name,
                                                product.categoryId,
                                                product.deliveryType,
                                                product.deliveryFee,
                                                product.refundFee,
                                                product.marketPrice,
                                                product.appSalesPrice,
                                                product.discountPrice,
                                                product.weight,
                                                product.type,
                                                product.productStatus,
                                                productDescription.description.as("description"),
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
                                                GroupBy.set(
                                                        Projections.constructor(
                                                                AttachFileDTO.class,
                                                                attachFile.id,
                                                                attachFile.attachFileName
                                                        )
                                                ).as("productImageList"),
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
                            existing.getProductImageList().addAll(replacement.getProductImageList());
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
        BooleanBuilder productBuilder = new BooleanBuilder()
                .and(product.isDeleted.eq(false));

        // 카테고리가 검색에 존재할 때만 검색
        if (subCategoryId != null) {
            productBuilder
                    .and(product.categoryId.eq(subCategoryId));
        }

        List<ProductForAppDTO> content = findAllProduct(productBuilder, userId, pageable);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public List<ProductForAppDTO> findAllProduct(
            BooleanBuilder productBuilder,
            Long userId,
            Pageable pageable
    ) {
        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        BooleanBuilder wishlistBuilder = new BooleanBuilder();
        wishlistBuilder.and(wishlist.registerId.eq(userId.toString()))
                .and(wishlist.productId.eq(product.id));

        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                .leftJoin(wishlist).on(wishlistBuilder)
                .where(productBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return query
                .transform(
                        GroupBy.groupBy(product.id).list(Projections.constructor(
                                ProductForAppDTO.class,
                                product.categoryId,
                                        product.name,
                                        seller.marketName,
                                        product.appSalesPrice,
                                        product.deliveryType,
                                        product.discountPrice,
                                        GroupBy.list(Projections.list(Projections.constructor(
                                                                AttachFileDTO.class,
                                                                attachFile.id,
                                                                attachFile.attachFileName
                                                        )
                                                )
                                        ),
                                        wishlist.id,
                                        product.deliveryFee,
                                        product.type,
                                        product.createAt

                                )
                        )
                );
    }
}
