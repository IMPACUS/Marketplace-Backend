package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.common.QAttachFileGroup;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QWishlist;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishlistCustomRepositoryImpl implements WishlistCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProduct product = QProduct.product;
    private final QSeller seller = QSeller.seller;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QAttachFileGroup attachFileGroup = QAttachFileGroup.attachFileGroup;

    @Override
    public Slice<WishlistDetailDTO> findAllWishListByUserId(Long userId, Pageable pageable) {
        List<WishlistDetailDTO> dtos = getWishlistDetailDTOs(userId);

        return PaginationUtils.toSlice(dtos, pageable);
    }

    private List<WishlistDetailDTO> getWishlistDetailDTOs(Long userId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(wishlist.productId))
                .and(product.isDeleted.eq(false));

        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(ReferencedEntityType.PRODUCT))
                .and(attachFileGroup.referencedId.eq(product.id));

        return queryFactory
                .selectFrom(wishlist)
                .innerJoin(product).on(productBuilder)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .leftJoin(attachFile).on(attachFile.id.eq(attachFileGroup.attachFileId))
                .where(wishlist.registerId.eq(userId.toString()))
                .transform(
                        GroupBy.groupBy(wishlist.id).list(Projections.constructor(
                                WishlistDetailDTO.class,
                                wishlist.id,
                                product.id,
                                product.name,
                                seller.marketName,
                                product.appSalesPrice,
                                product.deliveryType,
                                product.discountPrice,
                                product.deliveryFee,
                                product.type,
                                product.createAt,
                                GroupBy.list(Projections.list(Projections.constructor(
                                                        AttachFileDTO.class,
                                                        attachFile.id,
                                                        attachFile.attachFileName
                                                )
                                        )
                                )
                        ))
                );
    }
}
