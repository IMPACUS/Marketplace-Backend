package com.impacus.maketplace.repository.review.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.dto.review.response.*;
import com.impacus.maketplace.entity.payment.QPaymentOrder;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.review.QReview;
import com.impacus.maketplace.entity.review.QReviewReply;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
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
    private final QProduct product = QProduct.product;

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

        // 현재 사용자 정보 가져오기
        UserType currentUserType = SecurityUtils.getCurrentUserType();
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // 데이터 조회
        JPAQuery<WebReviewDTO> reviewQuery = getQueryToFindReviews();
        JPAQuery<Long> countQuery = queryFactory
                .select(review.id.count())
                .from(review);

        if (SecurityUtils.getCurrentUserType() == currentUserType) {
            reviewBoolean.and(review.isDeleted.isFalse());

            Long sellerId = fetchSellerId(currentUserId);

            addSellerConditions(reviewQuery, sellerId);
            addSellerConditions(countQuery, sellerId);
        }


        List<WebReviewDTO> dtos = reviewQuery
                .where(reviewBoolean)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createAt.desc())
                .fetch();

        long count = countQuery
                .where(reviewBoolean)
                .fetchFirst();

        return PaginationUtils.toPage(dtos, pageable, count);
    }

    private JPAQuery<WebReviewDTO> getQueryToFindReviews() {
        return queryFactory
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
                .leftJoin(user).on(user.id.eq(review.userId));
    }

    // 판매자 ID 가져오기
    private Long fetchSellerId(Long userId) {
        return queryFactory
                .select(user.id)
                .from(user)
                .where(user.id.eq(userId))
                .fetchFirst();
    }

    // 판매자 조건 및 조인 추가
    private void addSellerConditions(JPAQuery<?> query, Long sellerId) {
        query.innerJoin(product).on(product.sellerId.eq(sellerId))
                .innerJoin(productOption).on(productOption.productId.eq(product.id));
    }

    @Override
    public WebReviewDetailDTO findReview(Long reviewId) {
        return queryFactory
                .select(Projections
                        .constructor(
                                WebReviewDetailDTO.class,
                                review.id,
                                review.orderId,
                                product.productNumber,
                                user.name,
                                review.rating,
                                review.contents,
                                product.name,
                                review.images,
                                Projections.fields(
                                        ProductOptionDTO.class,
                                        productOption.id,
                                        productOption.color,
                                        productOption.size
                                ),
                                user.email,
                                review.createAt,
                                Projections.fields(
                                        WebReviewReplyDTO.class,
                                        reviewReply.id,
                                        reviewReply.contents
                                )
                        ))
                .innerJoin(productOption).on(productOption.id.eq(review.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId))
                .leftJoin(user).on(user.id.eq(review.userId))
                .leftJoin(reviewReply).on(reviewReply.reviewId.eq(review.id))
                .from(review)
                .where(review.id.eq(reviewId))
                .fetchFirst();
    }

    @Override
    public List<WebReviewDTO> findReviewsByIds(IdsDTO dto) {
        BooleanBuilder reviewBoolean = new BooleanBuilder()
                .and(review.id.in(dto.getIds()));

        JPAQuery<WebReviewDTO> reviewQuery = getQueryToFindReviews();

        return reviewQuery
                .where(reviewBoolean)
                .orderBy(review.createAt.desc())
                .fetch();
    }

    @Override
    public long cleanUpReview() {
        LocalDateTime now = LocalDateTime.now().minusDays(14);

        BooleanBuilder reviewBoolean = new BooleanBuilder()
                .and(review.isDeleted.isTrue())
                .and(review.modifyAt.after(now));

        // reviewReply 삭제
        queryFactory.delete(reviewReply)
                .where(reviewReply.reviewId.in(
                        JPAExpressions.select(review.id)
                                .from(review)
                                .where(reviewBoolean)
                ))
                .execute();

        // review 삭제
        return queryFactory.delete(review)
                .where(reviewBoolean)
                .execute();
    }
}
