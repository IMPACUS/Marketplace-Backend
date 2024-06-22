package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.ProductErrorEnum;
import com.impacus.maketplace.common.exception.CustomException;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
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

    @Override
    public Page<ProductForWebDTO> findAllProduct(
            Long sellerId,
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .and(product.sellerId.eq(sellerId));

        List<ProductForWebDTO> content = getProductDTO(builder, pageable);
        Long count = getProductDTOCount(builder);
        return new PageImpl<>(content, pageable, count);
    }

    private Long getProductDTOCount(BooleanBuilder builder) {
        return queryFactory.select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();
    }


    private List<ProductForWebDTO> getProductDTO(BooleanBuilder builder, Pageable pageable) {
        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        return queryFactory.selectFrom(product)
                .leftJoin(productOption).on(product.id.eq(productOption.productId))
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                .where(builder)
                .groupBy(product.id, productOption.id, attachFile.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        GroupBy.groupBy(product.id).list(Projections.constructor(
                                        ProductForWebDTO.class,
                                        product.id,
                                        product.name,
                                        product.appSalesPrice,
                                        product.productNumber,
                                        product.deliveryType,
                                        product.productStatus,
                                        productOption.stock.sum(),
                                        product.createAt,
                                        GroupBy.list(
                                                Projections.list(Projections.fields(
                                                                ProductOptionDTO.class,
                                                                productOption.id,
                                                                productOption.color,
                                                                productOption.size
                                                        )
                                                )
                                        ),
                                GroupBy.list(
                                        Projections.list(Projections.constructor(
                                                        AttachFileDTO.class,
                                                        attachFile.id,
                                                        attachFile.attachFileName
                                                )
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

        List<DetailedProductDTO> result = queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .leftJoin(description).on(description.productId.eq(product.id))
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
            throw new CustomException(ProductErrorEnum.NOT_EXISTED_PRODUCT);
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
    public Slice<ProductForAppDTO> findAllProductBySubCategoryId(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    ) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder
                .and(product.categoryId.eq(subCategoryId))
                .and(product.isDeleted.eq(false));

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
                                        product.id,
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
