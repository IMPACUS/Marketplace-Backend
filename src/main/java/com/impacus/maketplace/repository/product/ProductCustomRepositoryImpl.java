package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.product.response.DetailedProductDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductForWebDTO> findAllProduct(LocalDate startAt, LocalDate endAt, Pageable pageable) {
        List<ProductForWebDTO> content = getProductDTO(startAt, endAt, pageable);
        Long count = getProductDTOCount(startAt, endAt);
        return new PageImpl<>(content, pageable, count);
    }

    private Long getProductDTOCount(LocalDate startAt, LocalDate endAt) {
        return queryFactory.select(product.count())
                .from(product)
                .where(product.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .fetchOne();
    }


    private List<ProductForWebDTO> getProductDTO(LocalDate startAt, LocalDate endAt, Pageable pageable) {
        return queryFactory.selectFrom(product)
                .leftJoin(productOption).on(product.id.eq(productOption.productId))
                .where(product.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .groupBy(product.id, productOption.id)
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
}
