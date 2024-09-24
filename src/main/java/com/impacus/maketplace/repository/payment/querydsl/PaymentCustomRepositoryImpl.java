package com.impacus.maketplace.repository.payment.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

    private final JPAQueryFactory queryFactory;
}
