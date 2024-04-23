package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.dto.seller.response.QSimpleSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.QSellerBusinessInfo;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SellerCustomRepositoryImpl implements SellerCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QSeller seller = QSeller.seller;
    private final QUser user = QUser.user;
    private final QSellerBusinessInfo sellerBusinessInfo = QSellerBusinessInfo.sellerBusinessInfo;


    @Override
    public Page<SimpleSellerEntryDTO> findAllSellerWithEntry(LocalDate startAt, LocalDate endAt, Pageable pageable, EntryStatus[] entryStatus) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(seller.isDeleted.eq(false));
        builder.and(seller.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)));
        if (entryStatus != null) {
            builder.and(seller.entryStatus.in(Arrays.stream(entryStatus).toList()));
        }

        List<SimpleSellerEntryDTO> content = getSimpleSellerEntryDTO(builder);
        Long count = getSimpleSellerEntryDTOCount(builder);
        return new PageImpl<>(content, pageable, count);
    }

    private Long getSimpleSellerEntryDTOCount(BooleanBuilder builder) {
        return queryFactory.select(seller.count())
                .from(seller)
                .where(builder)
                .fetchOne();
    }

    public List<SimpleSellerEntryDTO> getSimpleSellerEntryDTO(BooleanBuilder builder) {
        List<SimpleSellerEntryDTO> a = queryFactory.select(
                        new QSimpleSellerEntryDTO(
                                user.id,
                                seller.createAt,
                                seller.marketName,
                                user.phoneNumber,
                                sellerBusinessInfo.businessCondition,
                                seller.entryStatus
                        )
                )
                .from(seller)
                .innerJoin(user).on(user.id.eq(seller.userId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .where(builder)
                .fetch();
        return a;
    }
}
