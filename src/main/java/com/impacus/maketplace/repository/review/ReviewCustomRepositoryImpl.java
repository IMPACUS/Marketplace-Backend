package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.dto.review.QReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewSellerDTO;
import com.impacus.maketplace.entity.order.QOrder;
import com.impacus.maketplace.entity.order.QPurchaseProduct;
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
    private final QOrder order = QOrder.order;
    private final QSeller seller = QSeller.seller;
    private final QPurchaseProduct purchaseProduct = QPurchaseProduct.purchaseProduct;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;


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
    public Slice<ReviewSellerDTO> displaySellerReviewList(Pageable pageable, Long userId) {
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
     *     private Long id; // 리뷰 인덱스 번호
     *     private Long orderId; // 주문 인덱스 번호
     *     private Long sellerId; // 판매자 인덱스 번호
     *     private Long buyerId; // 구매자 인덱스 번호
     *     private Integer score; // 점수
     *     private String buyerContents; // 구매자 리뷰 내용
     *     private Long buyerUploadImgId; // 구매자 업로드 이미지 번호
     *     private String sellerComment; // 판매자 답글
     *     private ZonedDateTime createAt; // 리뷰 생성일
     *     private Boolean isArchive; // 삭제 시 아카이브 여부
     *     private ZonedDateTime archiveAt; // 아카이브 시작점 (Spring 스케쥴링 기법으로 자동 삭제 여부 확인)
     *
     *     private String buyerName; // 주문자 표시 (웹 - 판매자 사이트)
     *     private String idName; // 주문자 아이디
     *
     *     // 아래는 product_detail_info
     *     private String productColor;
     *     private Long productId;
     *     private String productMaterial;
     *     private String productSize;
     *     private String productType;
     *
     *     // 아래는 purchase_product
     *     private Integer quantity;
     *     private Integer totalPrice;
     */

}
