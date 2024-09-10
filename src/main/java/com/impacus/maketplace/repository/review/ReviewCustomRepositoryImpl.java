package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.dto.review.QReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewSellerDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductDetailInfo;
import com.impacus.maketplace.entity.review.QReview;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
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
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QReview review = QReview.review;
    private final QUser userEntity = QUser.user;
//    private final QOrder order = QOrder.order;
    private final QSeller seller = QSeller.seller;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;
    private final QProduct product = QProduct.product;


    /**
     * 구매자 전용 - 해당 상품 구매한 리뷰 보기
     *
     * @param userId 회원 번호 (로그인)
     * @return
     */
    @Override
    public List<ReviewBuyerDTO> displayViewBuyerReview(Long userId) {
        /**
         *     @QueryProjection
         *     public ReviewBuyerDTO(Long id, Long orderId, Integer score, String buyerContents,
         *                    String imgSrc, String sellerComment) {
         *         this.id = id;
         *         this.orderId = orderId;
         *         this.score = score;
         *         this.buyerContents = buyerContents;
         *         this.buyerUploadImgId = buyerUploadImgId;
         *         this.sellerComment = sellerComment;
         *     }
         */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(review.buyerId.eq(userId)); // 추가된 조건

        List<ReviewBuyerDTO> reviewBuyerDTO = queryFactory.select(
                        new QReviewBuyerDTO(
                                review.id,
                                review.orderId,
                                review.score,
                                review.buyerContents,
                                review.buyerUploadImgId,
                                review.sellerComment
                        )
                ).from(review)
                .where(builder)
                .fetch();
        return reviewBuyerDTO;
    }

    /**
     * (2) 구매자용 리뷰 상세보기
     *
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public ReviewBuyerDTO displayViewBuyerReviewOne(Long userId, Long orderId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(review.orderId.eq(orderId));
        builder.and(review.buyerId.eq(userId)); // 추가된 조건

        ReviewBuyerDTO reviewBuyerDTO = queryFactory.select(
                        new QReviewBuyerDTO(
                                review.id,
                                review.orderId,
                                review.score,
                                review.buyerContents,
                                review.buyerUploadImgId,
                                review.sellerComment
                        )
                ).from(review)
                .where(builder)
                .fetchOne();
        return reviewBuyerDTO;
    }



    @Override
    public Slice<ReviewSellerDTO> displaySellerReviewList(Pageable pageable, Long userId, String search) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.trim() + "%";
            booleanBuilder.or(review.orderId.stringValue().likeIgnoreCase(searchPattern));
            booleanBuilder.or(review.sellerId.stringValue().likeIgnoreCase(searchPattern));
            booleanBuilder.or(review.buyerId.stringValue().likeIgnoreCase(searchPattern));
            booleanBuilder.or(review.score.stringValue().likeIgnoreCase(searchPattern));
            booleanBuilder.or(review.buyerContents.likeIgnoreCase(searchPattern));
            booleanBuilder.or(review.sellerComment.likeIgnoreCase(searchPattern));
            booleanBuilder.or(userEntity.name.likeIgnoreCase(searchPattern));
        }

        List<ReviewSellerDTO> results = queryFactory.select(
                        Projections.fields(ReviewSellerDTO.class,
                                review.id,
                                review.orderId,
                                review.sellerId,
                                review.buyerId,
                                review.score,
                                review.buyerContents,
                                review.sellerComment,
                                review.createAt,
                                review.buyerContents,
                                review.isComment,
                                userEntity.name
                        )
                ).from(review).innerJoin(userEntity).on(review.sellerId.eq(userEntity.id))
                .where(review.sellerId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(review.createAt.desc())
                .fetch();
        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 판매자 리뷰 상세 보기 - 리뷰 답글 달기용
     * @param userId
     * @param reviewId
     * @param sellerComment
     * @return
     */
    @Override
    public ReviewSellerDTO displaySellerReviewDetail(Long userId, Long reviewId, String sellerComment) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(review.id.eq(reviewId));
        builder.and(review.sellerId.eq(userId)); // 판매자 ID 조건 추가

        return queryFactory.select(
                        Projections.fields(
                                ReviewSellerDTO.class,
                                review.id,
                                review.orderId,
                                review.sellerId,
                                review.buyerId,
                                review.score,
                                review.buyerContents,
                                review.buyerUploadImgId,
                                review.sellerComment,
                                review.createAt,
                                review.isArchive,
                                review.archiveAt,
                                userEntity.name.as("buyerName"),
                                userEntity.userIdName.as("idName"),
                                productDetailInfo.productColor,
                                product.id.as("productId"),
                                productDetailInfo.productMaterial,
                                productDetailInfo.productSize,
                                productDetailInfo.productType
                        )
                ).from(review)
                .join(userEntity).on(review.buyerId.eq(userEntity.id))
//                .join(order).on(review.orderId.eq(order.id))
//                .join(product).on(order.id.eq(product.id))
                .join(productDetailInfo).on(product.id.eq(productDetailInfo.productId))
                .where(builder)
                .fetchOne();
    }
}
