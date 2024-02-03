package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.product.response.ProductDetailDTO;
import com.impacus.maketplace.dto.product.response.ProductForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;

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
                                        Projections.list(Projections.fields(
                                                        ProductOptionDTO.class,
                                                        productOption.id,
                                                        productOption.color,
                                                        productOption.size
                                                )
                                        )
                                )
                        )
                );
    }

    @Override
    public ProductDetailDTO findProductByProductId(Long productId) {
        JPAQuery<ProductDetailDTO> query = queryFactory.select(Projections.constructor(
                        ProductDetailDTO.class,
                        product.id,
                        product.name,
                        product.appSalesPrice,
                        product.discountPrice,
                        Projections.list(Projections.fields(
                                        ProductOptionDTO.class,
                                        productOption.id,
                                        productOption.color,
                                        productOption.size
                                )
                        )
                ))
                .from(product)
                .leftJoin(productOption).on(product.id.eq(productOption.productId))
                .where(product.id.eq(productId))
                .groupBy(product.id, productOption.id);

        return query.fetchOne();
    }
}
