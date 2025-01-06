package com.impacus.maketplace.repository.category.querydsl;

import com.impacus.maketplace.dto.category.dto.SearchCategoryDTO;
import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.category.QSuperCategory;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public List<CategoryDetailDTO> findSuperCategories(String keyword) {
        BooleanBuilder subCategoryBoolean = new BooleanBuilder();
        subCategoryBoolean.and(superCategory.id.eq(subCategory.superCategoryId));

        if (keyword != null && !keyword.isBlank()) {
            subCategoryBoolean.and(subCategory.name.containsIgnoreCase(keyword));
        }

        return queryFactory.selectFrom(superCategory)
                .leftJoin(subCategory).on(subCategoryBoolean)
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

    @Override
    public Slice<SearchCategoryDTO> findSuperCategoriesBy(PageRequest pageable) {
        // 데이터 조회
        List<SearchCategoryDTO> content = queryFactory
                .select(Projections.fields(
                        SearchCategoryDTO.class,
                        superCategory.id.as("categoryId"),
                        superCategory.name
                ))
                .from(superCategory)
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize() + 1) // 추가 데이터로 다음 페이지 여부 판단
                .fetch();

        // 다음 페이지 여부 체크
        boolean hasNext = content.size() > pageable.getPageSize();

        // 추가 데이터 제거
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<SearchCategoryDTO> findSubCategoriesBy(PageRequest pageable) {
        // 데이터 조회
        List<SearchCategoryDTO> content = queryFactory
                .select(Projections.fields(
                        SearchCategoryDTO.class,
                        subCategory.id.as("categoryId"),
                        subCategory.name
                ))
                .from(subCategory)
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize() + 1) // 추가 데이터로 다음 페이지 여부 판단
                .fetch();

        // 다음 페이지 여부 체크
        boolean hasNext = content.size() > pageable.getPageSize();

        // 추가 데이터 제거
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
