package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.common.QAttachFileGroup;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShoppingBasketCustomRepositoryImpl implements ShoppingBasketCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QShoppingBasket shoppingBasket = QShoppingBasket.shoppingBasket;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QSeller seller = QSeller.seller;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QAttachFileGroup attachFileGroup = QAttachFileGroup.attachFileGroup;

    @Override
    public Slice<ShoppingBasketDetailDTO> findAllShoppingBasketByUserId(Long userId, Pageable pageable) {
        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productOption.productId))
                .and(product.isDeleted.eq(false));

        List<ShoppingBasketDetailDTO> content = queryFactory
                .selectFrom(shoppingBasket)
                .leftJoin(productOption).on(shoppingBasket.productOptionId.eq(productOption.id))
                .innerJoin(product).on(productBuilder)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                .where(shoppingBasket.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        GroupBy.groupBy(shoppingBasket.id).list(Projections.constructor(
                                ShoppingBasketDetailDTO.class,
                                shoppingBasket.id,
                                shoppingBasket.quantity,
                                Projections.constructor(
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
                                        )
                                ),
                                Projections.constructor(
                                        ProductOptionDTO.class,
                                        productOption.id,
                                        productOption.color,
                                        productOption.size
                                )
                        ))
                );

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
