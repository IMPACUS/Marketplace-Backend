package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.common.response.QAttachFileDTO;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.common.QAttachFileGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachFileCustomRepositoryImpl implements AttachFileCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QAttachFileGroup attachFileGroup = QAttachFileGroup.attachFileGroup;

    @Override
    public List<AttachFileDTO> findAllAttachFileByReferencedId(Long referencedId, ReferencedEntityType referencedEntityType) {
        BooleanBuilder attachFileGroupBuilder = new BooleanBuilder();
        attachFileGroupBuilder.and(attachFileGroup.referencedEntity.eq(referencedEntityType))
                .and(attachFileGroup.referencedId.eq(referencedId));

        BooleanBuilder attachFileBuilder = new BooleanBuilder();
        attachFileBuilder.and(attachFile.id.eq(attachFileGroup.attachFileId));

        JPAQuery<AttachFileDTO> query = queryFactory.select(
                        new QAttachFileDTO(
                                attachFile.id,
                                attachFile.attachFileName
                        )
                )
                .from(attachFile)
                .leftJoin(attachFileGroup).on(attachFileGroupBuilder)
                .where(attachFileBuilder)
                .groupBy(attachFile.id);

        return query.orderBy(attachFile.createAt.desc()).fetch();
    }
}
