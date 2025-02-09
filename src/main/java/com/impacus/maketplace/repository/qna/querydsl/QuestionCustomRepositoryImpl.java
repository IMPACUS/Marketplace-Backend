package com.impacus.maketplace.repository.qna.querydsl;

import com.impacus.maketplace.common.enumType.searchCondition.QnAReviewSearchCondition;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.qna.response.ConsumerQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDetailDTO;
import com.impacus.maketplace.dto.review.QnaReviewSearchCondition;
import com.impacus.maketplace.dto.review.response.WebReplyDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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

import static com.impacus.maketplace.entity.product.QProduct.product;
import static com.impacus.maketplace.entity.product.QProductOption.productOption;
import static com.impacus.maketplace.entity.qna.QQuestion.question;
import static com.impacus.maketplace.entity.qna.QQuestionReply.questionReply;
import static com.impacus.maketplace.entity.review.QReview.review;
import static com.impacus.maketplace.entity.seller.QSeller.seller;
import static com.impacus.maketplace.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {
    private final AuditorAware<String> auditorProvider;
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteQuestionById(long questionId) {
        queryFactory.update(question)
                .set(question.isDeleted, true)
                .set(question.modifyAt, LocalDateTime.now())
                .set(question.modifyId, auditorProvider.getCurrentAuditor().orElse(null))
                .where(question.id.eq(questionId))
                .execute();
    }

    @Override
    public Slice<ConsumerQuestionDTO> findConsumerQuestions(Long userId, Pageable pageable) {
        List<ConsumerQuestionDTO> results = queryFactory.select(
                        Projections.constructor(
                                ConsumerQuestionDTO.class,
                                question.id,
                                question.orderId,
                                question.contents,
                                questionReply.contents,
                                question.images,
                                Projections.fields(
                                        ProductOptionDTO.class,
                                        productOption.id.as("productOptionId"),
                                        productOption.color,
                                        productOption.size
                                ),
                                question.createAt
                        )
                )
                .from(question)
                .leftJoin(questionReply).on(questionReply.questionId.eq(question.id))
                .innerJoin(productOption).on(productOption.id.eq(question.productOptionId))
                .where(question.userId.eq(userId).and(question.isDeleted.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .orderBy(question.createAt.desc())
                .fetch();

        return PaginationUtils.toSlice(results, pageable);
    }

    @Override
    public Page<WebQuestionDTO> findQuestions(QnaReviewSearchCondition condition) {
        // 현재 사용자 정보 가져오기
        UserType currentUserType = SecurityUtils.getCurrentUserType();
        Long currentUserId = SecurityUtils.getCurrentUserId();

        String keyword = condition.getKeyword();
        Pageable pageable = condition.getPageable();

        BooleanBuilder questionBoolean = new BooleanBuilder()
                .and(question.createAt.between(condition.getStartAt().atStartOfDay(), condition.getEndAt().atTime(LocalTime.MAX)));
        BooleanBuilder userBoolean = new BooleanBuilder();
        BooleanBuilder productBoolean = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            if (condition.getSearchCondition() == QnAReviewSearchCondition.ID) {
                userBoolean.and(user.email.containsIgnoreCase(keyword));
            } else if (condition.getSearchCondition() == QnAReviewSearchCondition.PRODUCT_NAME) {
                productBoolean.and(product.name.containsIgnoreCase(keyword));
            }
        }

        // 데이터 조회
        JPAQuery<WebQuestionDTO> questionQuery = getQueryToFindQuestions(userBoolean, productBoolean);
        JPAQuery<Long> countQuery = queryFactory
                .select(question.id.count())
                .from(question)
                .innerJoin(productOption).on(productOption.id.eq(question.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId).and(productBoolean.getValue() != null ? productBoolean : Expressions.TRUE));

        if (userBoolean.getValue() != null) {
            countQuery.innerJoin(user).on(user.id.eq(question.userId).and(userBoolean));
        } else {
            countQuery.leftJoin(user).on(user.id.eq(question.userId));
        }

        if (currentUserType == UserType.ROLE_APPROVED_SELLER) {
            questionBoolean.and(question.isDeleted.isFalse());

            Long sellerId = fetchSellerId(currentUserId);

            addSellerConditions(questionQuery, sellerId);
            addSellerConditions(countQuery, sellerId);
        }

        List<WebQuestionDTO> data = questionQuery
                .where(questionBoolean)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.createAt.desc())
                .fetch();

        long count = Optional.ofNullable(countQuery
                .where(questionBoolean)
                .fetchFirst()).orElse(0L);

        return PaginationUtils.toPage(data, pageable, count);
    }

    @Override
    public WebQuestionDetailDTO findQuestion(Long questionId) {
        return queryFactory
                .select(Projections
                        .fields(
                                WebQuestionDetailDTO.class,
                                question.id.as("questionId"),
                                question.orderId,
                                product.productNumber,
                                question.contents,
                                product.name.as("productName"),
                                question.images,
                                product.productImages,
                                Projections.fields(
                                        ProductOptionDTO.class,
                                        productOption.id,
                                        productOption.color,
                                        productOption.size
                                ).as("option"),
                                user.email.as("userEmail"),
                                user.name.as("userName"),
                                question.createAt.as("createdAt"),
                                Projections.fields(
                                        WebReplyDTO.class,
                                        questionReply.id.as("replyId"),
                                        questionReply.contents
                                ).as("reply")
                        ))
                .from(question)
                .innerJoin(productOption).on(productOption.id.eq(question.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId))
                .leftJoin(user).on(user.id.eq(question.userId))
                .leftJoin(questionReply).on(questionReply.questionId.eq(question.id))
                .where(question.id.eq(questionId))
                .fetchFirst();
    }

    @Override
    public List<WebQuestionDTO> findQuestionsByIds(IdsDTO dto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(review.id.in(dto.getIds()));

        JPAQuery<WebQuestionDTO> reviewQuery = getQueryToFindQuestions();

        return reviewQuery
                .where(booleanBuilder)
                .orderBy(question.createAt.desc())
                .fetch();
    }

    private JPAQuery<WebQuestionDTO> getQueryToFindQuestions() {
        return getQueryToFindQuestions(new BooleanBuilder(), new BooleanBuilder());
    }

    private JPAQuery<WebQuestionDTO> getQueryToFindQuestions(
            @Nullable BooleanBuilder userBoolean,
            @Nullable BooleanBuilder productBoolean
    ) {
        JPAQuery<WebQuestionDTO> questionQuery = queryFactory
                .select(
                        Projections.fields(
                                WebQuestionDTO.class,
                                question.id.as("questionId"),
                                question.contents,
                                question.orderId,
                                user.name.as("userName"),
                                user.email.as("userEmail"),
                                question.createAt.as("createdAt"),
                                questionReply.isNotNull().as("hasReply")
                        )
                )
                .from(question)
                .leftJoin(questionReply).on(questionReply.questionId.eq(question.id))
                .innerJoin(productOption).on(productOption.id.eq(question.productOptionId))
                .innerJoin(product).on(product.id.eq(productOption.productId).and(productBoolean != null && productBoolean.getValue() != null ? productBoolean : Expressions.TRUE));

        if (userBoolean != null && userBoolean.getValue() != null) {
            questionQuery.innerJoin(user).on(user.id.eq(question.userId).and(userBoolean));
        } else {
            questionQuery.leftJoin(user).on(user.id.eq(question.userId));
        }

        return questionQuery;
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
                        .and(productOption.id.eq(question.productOptionId)));
    }

}
