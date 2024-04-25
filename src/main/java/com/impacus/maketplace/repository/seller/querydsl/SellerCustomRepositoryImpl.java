package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.dto.seller.response.DetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.QDetailedSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.QSimpleSellerEntryDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerEntryDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.QSellerAdjustmentInfo;
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
    private final QSellerAdjustmentInfo sellerAdjustmentInfo = QSellerAdjustmentInfo.sellerAdjustmentInfo;
    private final QAttachFile attachFile = QAttachFile.attachFile;


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

    @Override
    public DetailedSellerEntryDTO findDetailedSellerEntry(Long userId) {
        DetailedSellerEntryDTO detailedSellerEntryDTO = queryFactory.select(
                        new QDetailedSellerEntryDTO(
                                user.id,
                                seller.marketName,
                                seller.contactName,
                                user.email,
                                user.phoneNumber,
                                sellerBusinessInfo.businessRegistrationNumber,
                                sellerBusinessInfo.mailOrderBusinessReportNumber,
                                sellerBusinessInfo.businessAddress,
                                sellerAdjustmentInfo.bankCode,
                                sellerAdjustmentInfo.accountName,
                                sellerAdjustmentInfo.accountNumber
                        )
                )
                .from(seller)
                .innerJoin(user).on(user.id.eq(userId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(seller.userId.eq(userId))
                .fetch().get(0);

        AttachFile businessRegistration = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.userId.eq(userId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerBusinessInfo.copyBusinessRegistrationCertificateId))
                .fetchOne();

        AttachFile mailOrderBusinessReport = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.userId.eq(userId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerBusinessInfo.copyMainOrderBusinessReportCardId))
                .fetchOne();

        AttachFile bankBook = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.userId.eq(userId))
                .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerAdjustmentInfo.copyBankBookId))
                .fetchOne();

        detailedSellerEntryDTO.setBusinessRegistrationUrl(businessRegistration == null ? null : businessRegistration.getAttachFileName());
        detailedSellerEntryDTO.setMailOrderBusinessReportUrl(mailOrderBusinessReport == null ? null : mailOrderBusinessReport.getAttachFileName());
        detailedSellerEntryDTO.setBankBookUrl(bankBook == null ? null : bankBook.getAttachFileName());

        return detailedSellerEntryDTO;
    }

    private Long getSimpleSellerEntryDTOCount(BooleanBuilder builder) {
        return queryFactory.select(seller.count())
                .from(seller)
                .where(builder)
                .fetchOne();
    }

    public List<SimpleSellerEntryDTO> getSimpleSellerEntryDTO(BooleanBuilder builder) {
        return queryFactory.select(
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
    }
}
