package com.impacus.maketplace.repository.review.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.dto.review.response.ConsumerReviewDTO;
import com.impacus.maketplace.dto.review.response.ProductReviewDTO;
import com.impacus.maketplace.dto.review.response.WebReviewDTO;
import com.impacus.maketplace.entity.payment.QPaymentOrder;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.review.QReview;
import com.impacus.maketplace.entity.review.QReviewReply;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QReview review = QReview.review;
    private final QUser user = QUser.user;
    private final QProductOption productOption = QProductOption.productOption;
    private final QReviewReply reviewReply  = QReviewReply.reviewReply;
    private final QPaymentOrder paymentOrder = QPaymentOrder.paymentOrder;

    @Override
    public void deleteReview(Long reviewId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(review)
                .set(review.isDeleted, true)
                .set(review.modifyAt, LocalDateTime.now())
                .set(review.modifyId, currentAuditor)
                .where(review.id.eq(reviewId))
                .execute();
    }

    @Override
    public void restoreReview(Long reviewId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(review)
                .set(review.isDeleted, false)
                .set(review.modifyAt, LocalDateTime.now())
                .set(review.modifyId, currentAuditor)
                .where(review.id.eq(reviewId))
                .execute();
    }

    @Override
    public Page<ProductReviewDTO> findReviewsByProductId(Long productId, Pageable pageable) {
        BooleanBuilder reviewBoolean = new BooleanBuilder()
                .and(review.isDeleted.isFalse());
        BooleanBuilder productOptionBoolean = new BooleanBuilder()
                .and(productOption.productId.eq(productId))
                .and(productOption.id.eq(review.productOptionId));

        // 데이터 조회
        List<ProductReviewDTO> dtos = queryFactory
                .select(
                        Projections.constructor(
                                ProductReviewDTO.class,
                                review.id,
                                review.orderId,
                                review.rating,
                                review.contents,
                                review.images,
                                Projections.fields(
                                        ProductOptionDTO.class,
                                        review.productOptionId,
                                        productOption.size,
                                        productOption.color
                                ),
                                user.email,
                                review.createAt
                        )
                )
                .from(review)
                .leftJoin(user).on(user.id.eq(review.userId))
                .innerJoin(productOption).on(productOptionBoolean)
                .where(reviewBoolean)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createAt.desc())
                .fetch();

        //  페이지 객체로 변환
        long count = queryFactory
                .select(review.id.count())
                .from(review)
                .innerJoin(productOption).on(productOptionBoolean)
                .where(reviewBoolean)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

    @Override
    public void updateReview(Long reviewId, ReviewDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(review)
                .set(review.contents, dto.getContents())
                .set(review.rating, dto.getRating())
                .set(review.modifyAt, LocalDateTime.now())
                .set(review.modifyId, currentAuditor)
                .where(review.id.eq(reviewId))
                .execute();
    }

    @Override
    public void updateReviewImages(Long reviewId, List<String> images) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(review)
                .set(review.images, images)
                .set(review.modifyAt, LocalDateTime.now())
                .set(review.modifyId, currentAuditor)
                .where(review.id.eq(reviewId))
                .execute();
    }

    @Override
    public Slice<ConsumerReviewDTO> findUserReviews(Long userId, Pageable pageable) {
        List<ConsumerReviewDTO> results = queryFactory.select(
                        Projections.constructor(ConsumerReviewDTO.class,
                                review.id.as("reviewId"),
                                review.orderId,
                                review.contents,
                                reviewReply.contents.as("replyContents"),
                                review.images,
                                Projections.fields(
                                        ProductOptionDTO.class,
                                        productOption.id.as("productOptionId"),
                                        productOption.color,
                                        productOption.size
                                ),
                                review.createAt,
                                paymentOrder
                        )
                ).from(review)
                .leftJoin(reviewReply).on(reviewReply.reviewId.eq(review.id))
                .innerJoin(productOption).on(productOption.id.eq(review.productOptionId))
                .innerJoin(paymentOrder).on(paymentOrder.id.eq(review.id))
                .where(review.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(review.createAt.desc())
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return PaginationUtils.toSlice(results, pageable);
    }

    @Override
    public Page<WebReviewDTO> findReviews(
            Pageable pageable,
            String keyword,
            LocalDate startAt,
            LocalDate endAt
    ) {
        BooleanBuilder reviewBoolean = new BooleanBuilder()
                .and(review.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .and(review.contents.containsIgnoreCase(keyword));
        if (SecurityUtils.getCurrentUserType() == UserType.ROLE_APPROVED_SELLER) {
            reviewBoolean.and(review.isDeleted.isFalse());
        }

        // 데이터 조회
        List<WebReviewDTO> dtos = queryFactory
                .select(
                        Projections.constructor(
                                WebReviewDTO.class,
                                review.id,
                                review.rating,
                                review.contents,
                                user.name,
                                user.email,
                                review.createAt,
                                review.isDeleted
                        )
                )
                .from(review)
                .leftJoin(user).on(user.id.eq(review.userId))
                .where(reviewBoolean)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createAt.desc())
                .fetch();

        //  페이지 객체로 변환
        long count = queryFactory
                .select(review.id.count())
                .from(review)
                .where(reviewBoolean)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

//
//    /**
//     * (2) 구매자용 리뷰 상세보기
//     *
//     * @param userId
//     * @param orderId
//     * @return
//     */
//    @Override
//    public ReviewBuyerDTO displayViewBuyerReviewOne(Long userId, Long orderId) {
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(review.orderId.eq(orderId));
//        builder.and(review.buyerId.eq(userId)); // 추가된 조건
//
//        ReviewBuyerDTO reviewBuyerDTO = queryFactory.select(
//                        new QReviewBuyerDTO(
//                                review.id,
//                                review.orderId,
//                                review.score,
//                                review.buyerContents,
//                                review.buyerUploadImgId,
//                                review.sellerComment
//                        )
//                ).from(review)
//                .where(builder)
//                .fetchOne();
//        return reviewBuyerDTO;
//    }
//
//
//
//    @Override
//    public Slice<ReviewSellerDTO> displaySellerReviewList(Pageable pageable, Long userId, String search) {
//        BooleanBuilder booleanBuilder = new BooleanBuilder();
//        if (search != null && !search.trim().isEmpty()) {
//            String searchPattern = "%" + search.trim() + "%";
//            booleanBuilder.or(review.orderId.stringValue().likeIgnoreCase(searchPattern));
//            booleanBuilder.or(review.sellerId.stringValue().likeIgnoreCase(searchPattern));
//            booleanBuilder.or(review.buyerId.stringValue().likeIgnoreCase(searchPattern));
//            booleanBuilder.or(review.score.stringValue().likeIgnoreCase(searchPattern));
//            booleanBuilder.or(review.buyerContents.likeIgnoreCase(searchPattern));
//            booleanBuilder.or(review.sellerComment.likeIgnoreCase(searchPattern));
//            booleanBuilder.or(userEntity.name.likeIgnoreCase(searchPattern));
//        }
//
//        List<ReviewSellerDTO> results = queryFactory.select(
//                        Projections.fields(ReviewSellerDTO.class,
//                                review.id,
//                                review.orderId,
//                                review.sellerId,
//                                review.buyerId,
//                                review.score,
//                                review.buyerContents,
//                                review.sellerComment,
//                                review.createAt,
//                                review.buyerContents,
//                                review.isComment,
//                                userEntity.name
//                        )
//                ).from(review).innerJoin(userEntity).on(review.sellerId.eq(userEntity.id))
//                .where(review.sellerId.eq(userId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize() + 1)
//                .orderBy(review.createAt.desc())
//                .fetch();
//        boolean hasNext = results.size() > pageable.getPageSize();
//        if (hasNext) {
//            results.remove(results.size() - 1);
//        }
//        return new SliceImpl<>(results, pageable, hasNext);
//    }
//
//    /**
//     * 판매자 리뷰 상세 보기 - 리뷰 답글 달기용
//     * @param userId
//     * @param reviewId
//     * @param sellerComment
//     * @return
//     */
//    @Override
//    public ReviewSellerDTO displaySellerReviewDetail(Long userId, Long reviewId, String sellerComment) {
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(review.id.eq(reviewId));
//        builder.and(review.sellerId.eq(userId)); // 판매자 ID 조건 추가
//
//        return queryFactory.select(
//                        Projections.fields(
//                                ReviewSellerDTO.class,
//                                review.id,
//                                review.orderId,
//                                review.sellerId,
//                                review.buyerId,
//                                review.score,
//                                review.buyerContents,
//                                review.buyerUploadImgId,
//                                review.sellerComment,
//                                review.createAt,
//                                review.isArchive,
//                                review.archiveAt,
//                                userEntity.name.as("buyerName"),
//                                userEntity.userIdName.as("idName"),
//                                productDetailInfo.productColor,
//                                product.id.as("productId"),
//                                productDetailInfo.productMaterial,
//                                productDetailInfo.productSize,
//                                productDetailInfo.productType
//                        )
//                ).from(review)
//                .join(userEntity).on(review.buyerId.eq(userEntity.id))
////                .join(order).on(review.orderId.eq(order.id))
////                .join(product).on(order.id.eq(product.id))
//                .join(productDetailInfo).on(product.id.eq(productDetailInfo.productId))
//                .where(builder)
//                .fetchOne();
//    }
}
