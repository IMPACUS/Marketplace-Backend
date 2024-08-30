package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.repository.coupon.querydsl.dto.ProductPricingInfoDTO;
import com.impacus.maketplace.repository.coupon.querydsl.dto.QProductPricingInfoDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponProductRepositoryImpl implements CouponProductRepository {

    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;
    private final QSeller seller = QSeller.seller;

    @Override
    public List<ProductPricingInfoDTO> findProductPricingInfoList(List<Long> productIdList) {
        return queryFactory
                .select(new QProductPricingInfoDTO(
                        product.id,
                        product.type,
                        product.appSalesPrice,
                        seller.marketName
                ))
                .from(product)
                .join(seller).on(seller.id.eq(product.sellerId))
                .where(product.id.in(productIdList))
                .fetch();
    }
}
