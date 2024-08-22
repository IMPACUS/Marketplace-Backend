package com.impacus.maketplace.repository.order.querydsl;

import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsDTO;
import com.impacus.maketplace.repository.order.querydsl.dto.QOrderProductWithDetailsDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;

    private final QProductOption productOption = QProductOption.productOption;
    private final QSeller seller = QSeller.seller;

    @Override
    public OrderProductWithDetailsDTO findOrderProductWithDetails(Long productId, Long productOptionId) {
        return queryFactory
                .select(new QOrderProductWithDetailsDTO(
                        product.name,
                        product.productStatus,
                        product.type,
                        product.discountStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.deliveryFee,
                        product.productImages,
                        product.isDeleted,
                        seller.marketName,
                        productOption.color,
                        productOption.size,
                        productOption.stock,
                        productOption.isDeleted
                ))
                .from(product)
                .join(seller).on(seller.id.eq(product.sellerId))
                .join(productOption).on(productOption.id.eq(productOptionId))
                .fetchOne();
    }

    @Override
    public List<OrderProductWithDetailsByCartDTO> findOrderProductWithDetailsByCart(Long userId, List<Long> productIdList) {
        return null;
    }
}
