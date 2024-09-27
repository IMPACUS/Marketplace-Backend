package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.dto.qna.ProductQuestionSpec;
import com.impacus.maketplace.entity.qna.ProductQuestion;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.api.PaymentEventInterface;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.impacus.maketplace.entity.qna.QAnswer.answer;
import static com.impacus.maketplace.entity.qna.QProductQuestion.productQuestion;

@Repository
@RequiredArgsConstructor
public class ProductQuestionCustomRepositoryImpl implements ProductQuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final PaymentEventInterface paymentEventInterface;

    private final UserRepository userRepository;

    /**
     * 문의 조건으로 상세 조회
     */
    @Override
    public Page<ProductQuestion> findByParams(ProductQuestionSpec spec, Pageable pageable) {
        BooleanExpression expression = productQuestion.sellerId.eq(spec.getSellerId());

        if (spec.getStartDate() != null) {
            expression.and(productQuestion.createAt.goe(spec.getStartDate().atStartOfDay()));
        }

        if (spec.getEndDate() != null) {
            expression.and(productQuestion.createAt.lt(spec.getEndDate().plusDays(1).atStartOfDay()));
        }

        if (spec.getAnswered() != null) {
            expression.and(spec.getAnswered() ? answer.questionId.isNotNull() : answer.questionId.isNull());
        }

        if (StringUtils.isNotBlank(spec.getAuthorId())) {
            List<User> users = userRepository.findByEmailLike(spec.getAuthorId());
            expression.and(productQuestion.userId.in(users.stream().map(User::getId).toList()));
        }

        if (StringUtils.isNotBlank(spec.getOrderNumber())) {
            Long paymentEventId = paymentEventInterface.findIdByOrderId(spec.getOrderNumber());
            if (paymentEventId == null) {
                expression.and(Expressions.asBoolean(false));
            } else {
                expression.and(productQuestion.paymentEventId.eq(paymentEventId));
            }
        }

        List<ProductQuestion> contents = jpaQueryFactory
                .select(productQuestion)
                .from(productQuestion)
                .leftJoin(answer).on(answer.questionId.eq(productQuestion.id))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> count = jpaQueryFactory.select(productQuestion.count())
                .where(expression);

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
    }

}
