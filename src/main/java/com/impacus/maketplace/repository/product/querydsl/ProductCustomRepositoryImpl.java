package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QSubCategory subCategory = QSubCategory.subCategory;

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
        return queryFactory.selectFrom(product)
                .leftJoin(productOption).on(product.id.eq(productOption.productId))
                .where(builder)
                .groupBy(product.id, productOption.id)
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
                                        )
                                )
                        )
                );
    }

    @Override
    public DetailedProductDTO findProductByProductId(Long productId) {
        List<DetailedProductDTO> result = queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .where(product.id.eq(productId))
                .transform(GroupBy.groupBy(product.id).list(
                                Projections.constructor(
                                        DetailedProductDTO.class,
                                        product.id,
                                        product.name,
                                        product.appSalesPrice,
                                        product.discountPrice,
                                        product.type,
                                        GroupBy.list(
                                                Projections.list(Projections.constructor(
                                                                ProductOptionDTO.class,
                                                                productOption.id,
                                                                productOption.color,
                                                                productOption.size
                                                        )
                                                )
                                        )
                                )
                        )
                );
        return result.isEmpty() ? null : result.get(0);
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
    public Slice<ProductForAppDTO> findAllProductBySubCategoryId(Long subCategoryId, Pageable pageable) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder
                .and(product.categoryId.eq(subCategoryId))
                .and(product.isDeleted.eq(false));

        List<ProductForAppDTO> content = queryFactory
                .selectFrom(product)
                .where(productBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        GroupBy.groupBy(product.id).list(Projections.constructor(
                                ProductForAppDTO.class,
                                        product.id,
                                        product.name,
                                        product.appSalesPrice,
                                        product.productNumber,
                                        product.deliveryType,
                                        product.type,
                                        product.createAt
                                )
                        )
                );

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
