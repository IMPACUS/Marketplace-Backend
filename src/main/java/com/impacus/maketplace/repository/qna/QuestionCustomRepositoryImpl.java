package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.qna.ProductQuestionSpec;
import com.impacus.maketplace.dto.qna.response.ConsumerQuestionDTO;
import com.impacus.maketplace.entity.qna.Question;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.api.PaymentEventInterface;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.impacus.maketplace.entity.product.QProductOption.productOption;
import static com.impacus.maketplace.entity.qna.QQuestion.question;
import static com.impacus.maketplace.entity.qna.QQuestionReply.questionReply;

@Repository
@RequiredArgsConstructor
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {
    private final AuditorAware<String> auditorProvider;
    private final JPAQueryFactory queryFactory;

    private final PaymentEventInterface paymentEventInterface;

    private final UserRepository userRepository;

    /**
     * 문의 조건으로 상세 조회
     */
    @Override
    public Page<Question> findByParams(ProductQuestionSpec spec, Pageable pageable) {
        BooleanExpression expression = question.isDeleted.isFalse();

        if (spec.getStartDate() != null) {
            expression.and(question.createAt.goe(spec.getStartDate().atStartOfDay()));
        }

        if (spec.getEndDate() != null) {
            expression.and(question.createAt.lt(spec.getEndDate().plusDays(1).atStartOfDay()));
        }

        if (spec.getAnswered() != null) {
            expression.and(spec.getAnswered() ? questionReply.questionId.isNotNull() : questionReply.questionId.isNull());
        }

        if (StringUtils.isNotBlank(spec.getAuthorId())) {
            User user = userRepository.findByEmailLikeAndIsDeletedFalse(spec.getAuthorId()).get();
            expression.and(question.userId.eq(user.getId()));
        }

        if (StringUtils.isNotBlank(spec.getOrderNumber())) {
            Long paymentEventId = paymentEventInterface.findIdByPaymentId(spec.getOrderNumber());
            if (paymentEventId == null) {
                expression.and(Expressions.asBoolean(false));
            } else {
                expression.and(question.orderId.eq(paymentEventId));
            }
        }

        List<Question> contents = queryFactory
                .select(question)
                .from(question)
                .leftJoin(questionReply).on(questionReply.questionId.eq(question.id))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> count = queryFactory.select(question.count())
                .where(expression);

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
    }

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

}
