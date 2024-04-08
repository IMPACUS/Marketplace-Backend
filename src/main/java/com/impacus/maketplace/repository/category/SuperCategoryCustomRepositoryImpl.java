package com.impacus.maketplace.repository.category;

import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.category.QSuperCategory;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuperCategoryCustomRepositoryImpl implements SuperCategoryCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QSuperCategory superCategory = QSuperCategory.superCategory;
    private final QSubCategory subCategory = QSubCategory.subCategory;
    private final QAttachFile attachFile = QAttachFile.attachFile;

    @Override
    public List<CategoryDetailDTO> findAllCategory() {
        return queryFactory.selectFrom(superCategory)
                .leftJoin(subCategory).on(superCategory.id.eq(subCategory.superCategoryId))
                .transform(
                        GroupBy.groupBy(superCategory.id).list(Projections.constructor(
                                        CategoryDetailDTO.class,
                                        superCategory.id,
                                        superCategory.name,
                                        GroupBy.list(
                                                Projections.constructor(
                                                        SubCategoryDetailDTO.class,
                                                        subCategory.id,
                                                        subCategory.name,
                                                        ExpressionUtils.as(
                                                                JPAExpressions.select(attachFile.attachFileName)
                                                                        .from(attachFile)
                                                                        .where(attachFile.id.eq(subCategory.thumbnailId))
                                                                , "thumbnailUrl"
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }
}
