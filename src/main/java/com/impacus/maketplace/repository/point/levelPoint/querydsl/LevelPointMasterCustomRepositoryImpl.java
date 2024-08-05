package com.impacus.maketplace.repository.point.levelPoint.querydsl;

import com.impacus.maketplace.dto.point.levelPoint.LevelPointDTO;
import com.impacus.maketplace.entity.point.levelPoint.QLevelPointMaster;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LevelPointMasterCustomRepositoryImpl implements LevelPointMasterCustomRepository {
    private final JPAQueryFactory queryFactory;

    private final QLevelPointMaster levelPointMaster = QLevelPointMaster.levelPointMaster;

    @Override
    public LevelPointDTO findPointInformationByUserId(Long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LevelPointDTO.class,
                                levelPointMaster.userLevel,
                                levelPointMaster.levelPoint
                        )
                )
                .from(levelPointMaster)
                .where(levelPointMaster.userId.eq(userId))
                .fetchFirst();
    }
}
