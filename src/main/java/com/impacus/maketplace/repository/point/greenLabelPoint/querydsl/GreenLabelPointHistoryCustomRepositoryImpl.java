package com.impacus.maketplace.repository.point.greenLabelPoint.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GreenLabelPointHistoryCustomRepositoryImpl implements GreenLabelPointHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
}
