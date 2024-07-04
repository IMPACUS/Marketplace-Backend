package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.dto.review.QReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.entity.order.QOrder;
import com.impacus.maketplace.entity.order.QPurchaseProduct;
import com.impacus.maketplace.entity.product.QProductDetailInfo;
import com.impacus.maketplace.entity.review.QReview;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QReview review = QReview.review;
    private final QUser userEntity = QUser.user;
    private final QOrder order = QOrder.order;
    private final QSeller seller = QSeller.seller;
    private final QPurchaseProduct purchaseProduct = QPurchaseProduct.purchaseProduct;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;


    /**
     * 구매자 전용 - 해당 상품 구매한 리뷰 보기
     * @param userId 회원 번호 (로그인)
     * @param orderId 주문 번호 인덱스
     * @return
     */
    @Override
    public List<ReviewBuyerDTO> displayViewBuyerReview(Long userId, Long orderId) {
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
        builder.and(review.orderId.eq(orderId));
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
}
