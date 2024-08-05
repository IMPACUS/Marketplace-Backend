package com.impacus.maketplace.repository.point.levelPoint.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LevelPointMasterCustomRepositoryImpl implements LevelPointMasterCustomRepository {
    private final JPAQueryFactory queryFactory;
}
