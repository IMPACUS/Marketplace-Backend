package com.impacus.maketplace.repository.review.querydsl;

import com.impacus.maketplace.common.enumType.searchCondition.QnAReviewSearchCondition;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.review.QnaReviewSearchCondition;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.dto.review.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.impacus.maketplace.entity.payment.QPaymentOrder.paymentOrder;
import static com.impacus.maketplace.entity.product.QProduct.product;
import static com.impacus.maketplace.entity.product.QProductOption.productOption;
import static com.impacus.maketplace.entity.review.QReview.review;
import static com.impacus.maketplace.entity.review.QReviewReply.reviewReply;
import static com.impacus.maketplace.entity.seller.QSeller.seller;
import static com.impacus.maketplace.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

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
        List<ProductReviewDTO> data = queryFactory
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
        long count = Optional.ofNullable(queryFactory
                .select(review.id.count())
                .from(review)
                .innerJoin(productOption).on(productOptionBoolean)
                .where(reviewBoolean)
                .fetchFirst()).orElse(0L);

        return PaginationUtils.toPage(data, pageable, count);
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
                .limit(pageable.getPageSize() + 1L)
                .orderBy(review.createAt.desc())
                .fetch();

        return PaginationUtils.toSlice(results, pageable);
    }

    @Override
    public Page<WebReviewDTO> findReviews(QnaReviewSearchCondition condition) {
        String keyword = condition.getKeyword();
        Pageable pageable = condition.getPageable();

        BooleanBuilder reviewBoolean = new BooleanBuilder()
                .and(review.createAt.between(condition.getStartAt().atStartOfDay(), condition.getEndAt().atTime(LocalTime.MAX)));
        BooleanBuilder userBoolean = new BooleanBuilder();
        BooleanBuilder productBoolean = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            if (condition.getSearchCondition() == QnAReviewSearchCondition.ID) {
                userBoolean.and(user.email.containsIgnoreCase(keyword));
            } else if (condition.getSearchCondition() == QnAReviewSearchCondition.PRODUCT_NAME) {
                productBoolean.and(product.name.containsIgnoreCase(keyword));
            }
        }

        // 현재 사용자 정보 가져오기
        UserType currentUserType = SecurityUtils.getCurrentUserType();
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // 데이터 조회
        JPAQuery<WebReviewDTO> reviewQuery = getQueryToFindReviews(userBoolean, productBoolean);
        JPAQuery<Long> countQuery = queryFactory
                .select(review.id.count())
                .from(review)
                .innerJoin(productOption).on(productOption.id.eq(review.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId).and(productBoolean.getValue() != null ? productBoolean : Expressions.TRUE));

        if (currentUserType == UserType.ROLE_APPROVED_SELLER) {
            reviewBoolean.and(review.isDeleted.isFalse());

            Long sellerId = fetchSellerId(currentUserId);

            addSellerConditions(reviewQuery, sellerId);
            addSellerConditions(countQuery, sellerId);
        }

        if (userBoolean.getValue() != null) {
            countQuery.innerJoin(user).on(user.id.eq(review.userId).and(userBoolean));
        } else {
            countQuery.leftJoin(user).on(user.id.eq(review.userId));
        }

        List<WebReviewDTO> data = reviewQuery
                .where(reviewBoolean)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createAt.desc())
                .fetch();

        long count = Optional.ofNullable(countQuery
                .where(reviewBoolean)
                .fetchFirst()).orElse(0L);

        return PaginationUtils.toPage(data, pageable, count);
    }

    private JPAQuery<WebReviewDTO> getQueryToFindReviews() {
        return getQueryToFindReviews(new BooleanBuilder(), new BooleanBuilder());
    }

    private JPAQuery<WebReviewDTO> getQueryToFindReviews(
            @Nullable BooleanBuilder userBoolean,
            @Nullable BooleanBuilder productBoolean
    ) {
        JPAQuery<WebReviewDTO> reviewQuery = queryFactory
                .select(
                        Projections.constructor(
                                WebReviewDTO.class,
                                review.id,
                                review.rating,
                                review.contents,
                                user.name,
                                user.email,
                                review.createAt,
                                review.isDeleted,
                                review.images
                        )
                )
                .from(review)
                .innerJoin(productOption).on(productOption.id.eq(review.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId).and(productBoolean != null && productBoolean.getValue() != null ? productBoolean : Expressions.TRUE));

        if (userBoolean != null && userBoolean.getValue() != null) {
            reviewQuery.innerJoin(user).on(user.id.eq(review.userId).and(userBoolean));
        } else {
            reviewQuery.leftJoin(user).on(user.id.eq(review.userId));
        }

        return reviewQuery;
    }

    // 판매자 ID 가져오기
    private Long fetchSellerId(Long userId) {
        return queryFactory
                .select(seller.id)
                .from(seller)
                .where(seller.userId.eq(userId))
                .fetchFirst();
    }

    // 판매자 조건 및 조인 추가
    private void addSellerConditions(JPAQuery<?> query, Long sellerId) {
        query.innerJoin(product).on(product.sellerId.eq(sellerId))
                .innerJoin(productOption).on(productOption.productId.eq(product.id)
                        .and(productOption.id.eq(review.productOptionId)));
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
                                        reviewReply.id.as("reviewReplyId"),
                                        reviewReply.contents
                                )
                        ))
                .from(review)
                .innerJoin(productOption).on(productOption.id.eq(review.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId))
                .leftJoin(user).on(user.id.eq(review.userId))
                .leftJoin(reviewReply).on(reviewReply.reviewId.eq(review.id))
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
                .and(review.modifyAt.before(now));

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
