package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.seller.SellerType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.dto.category.response.SubCategoryDetailDTO;
import com.impacus.maketplace.dto.seller.response.*;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.common.QAttachFile;
import com.impacus.maketplace.entity.seller.QBrand;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.QSellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.QSellerBusinessInfo;
import com.impacus.maketplace.entity.seller.delivery.QSelectedSellerDeliveryAddress;
import com.impacus.maketplace.entity.seller.delivery.QSellerDeliveryAddress;
import com.impacus.maketplace.entity.seller.deliveryCompany.QSelectedSellerDeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.QSellerDeliveryCompany;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.entity.user.QUserStatusInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReadSellerCustomRepositoryImpl implements ReadSellerCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QSeller seller = QSeller.seller;
    private final QUser user = QUser.user;
    private final QUserStatusInfo userStatusInfo = QUserStatusInfo.userStatusInfo;
    private final QSellerBusinessInfo sellerBusinessInfo = QSellerBusinessInfo.sellerBusinessInfo;
    private final QSellerAdjustmentInfo sellerAdjustmentInfo = QSellerAdjustmentInfo.sellerAdjustmentInfo;
    private final QAttachFile attachFile = QAttachFile.attachFile;
    private final QBrand brand = QBrand.brand;
    private final QSelectedSellerDeliveryAddress selectedSellerDeliveryAddress = QSelectedSellerDeliveryAddress.selectedSellerDeliveryAddress;
    private final QSellerDeliveryAddress sellerDeliveryAddress = QSellerDeliveryAddress.sellerDeliveryAddress;
    private final QSellerDeliveryCompany sellerDeliveryCompany = QSellerDeliveryCompany.sellerDeliveryCompany;
    private final QSelectedSellerDeliveryCompany selectedSellerDeliveryCompany = QSelectedSellerDeliveryCompany.selectedSellerDeliveryCompany;

    @Override
    public Page<SimpleSellerEntryDTO> findAllSellerWithEntry(
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable,
            EntryStatus[] entryStatus,
            String brandName
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(seller.isDeleted.eq(false))
                .and(seller.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
                .and(checkIsContainBrandName(brandName));
        if (entryStatus != null) {
            builder.and(seller.entryStatus.in(Arrays.stream(entryStatus).toList()));
        }


        List<SimpleSellerEntryDTO> content = getSimpleSellerEntryDTO(builder);
        Long count = getSimpleSellerEntryDTOCount(builder);
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public DetailedSellerEntryDTO findDetailedSellerEntry(Long userId) {
        DetailedSellerEntryDTO detailedSellerEntryDTO = queryFactory
                .select(
                        Projections.fields(
                                DetailedSellerEntryDTO.class,
                                user.id,
                                seller.marketName,
                                seller.contactName,
                                user.email,
                                user.phoneNumber.as("contactNumber"),
                                sellerBusinessInfo.businessRegistrationNumber,
                                sellerBusinessInfo.mailOrderBusinessReportNumber,
                                sellerBusinessInfo.businessAddress,
                                sellerAdjustmentInfo.bankCode,
                                sellerAdjustmentInfo.accountName,
                                sellerAdjustmentInfo.accountNumber,
                                attachFile.attachFileName.as("logoImageUrl"),
                                seller.chargePercent,
                                seller.entryStatus
                        )
                )
                .from(seller)
                .innerJoin(user).on(user.id.eq(userId))
                .innerJoin(attachFile).on(attachFile.id.eq(seller.logoImageId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(seller.userId.eq(userId))
                .fetchFirst();

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

        AttachFile bankBookUrl = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.userId.eq(userId))
                .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerAdjustmentInfo.copyBankBookId))
                .fetchOne();

        detailedSellerEntryDTO.setBusinessRegistrationUrl(businessRegistration == null ? null : businessRegistration.getAttachFileName());
        detailedSellerEntryDTO.setMailOrderBusinessReportUrl(mailOrderBusinessReport == null ? null : mailOrderBusinessReport.getAttachFileName());
        detailedSellerEntryDTO.setBankBookUrl(bankBookUrl == null ? null : bankBookUrl.getAttachFileName());

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

    @Override
    public DetailedSellerDTO findDetailedSellerInformationByUserId(Long userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(seller.userId.eq(userId))
                .and(seller.isDeleted.eq(false));

        List<DetailedSellerDTO> dtos = queryFactory.selectFrom(seller)
                .where(builder)
                .leftJoin(attachFile).on(attachFile.id.eq(seller.logoImageId))
                .leftJoin(brand).on(brand.sellerId.eq(seller.id))
                .leftJoin(user).on(user.id.eq(seller.userId))
                .leftJoin(selectedSellerDeliveryAddress).on(selectedSellerDeliveryAddress.sellerId.eq(seller.id))
                .leftJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .leftJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .leftJoin(sellerDeliveryAddress).on(sellerDeliveryAddress.sellerId.eq(seller.id))
                .leftJoin(sellerDeliveryCompany).on(sellerDeliveryCompany.sellerId.eq(seller.id))
                .leftJoin(selectedSellerDeliveryCompany).on(selectedSellerDeliveryCompany.sellerDeliveryCompanyId.eq(sellerDeliveryCompany.id))
                .transform(
                        GroupBy.groupBy(seller.id).list(
                                Projections.fields(
                                        DetailedSellerDTO.class,
                                        attachFile.attachFileName.as("logoImageUrl"),
                                        seller.marketName.as("brandName"),
                                        seller.customerServiceNumber,
                                        sellerBusinessInfo.businessEmail.as("representativeEmail"),
                                        brand.introduction.as("brandIntroduction"),
                                        brand.openingTime,
                                        brand.closingTime,
                                        brand.businessDay,
                                        brand.breakingTime,
                                        user.email,
                                        user.phoneNumber,
                                        Projections.fields(
                                                SellerManagerInfoDTO.class,
                                                sellerBusinessInfo.representativeName,
                                                sellerBusinessInfo.businessAddress.as("address"),
                                                sellerBusinessInfo.businessRegistrationNumber,
                                                sellerBusinessInfo.mailOrderBusinessReportNumber
                                        ).as("manager"),
                                        Projections.fields(
                                                SellerAdjustmentInfoDTO.class,
                                                sellerAdjustmentInfo.bankCode,
                                                sellerAdjustmentInfo.accountName,
                                                sellerAdjustmentInfo.accountNumber
                                        ).as("adjustment"),
                                        Projections.fields(
                                                SellerDeliveryCompanyInfoDTO.class,
                                                sellerDeliveryCompany.generalDeliveryFee,
                                                sellerDeliveryCompany.generalSpecialDeliveryFee,
                                                sellerDeliveryCompany.refundDeliveryFee,
                                                sellerDeliveryCompany.refundSpecialDeliveryFee,
                                                GroupBy.list(
                                                        Projections.fields(
                                                                SelectedSellerDeliveryCompanyDTO.class,
                                                                selectedSellerDeliveryCompany.id.as("selectedSellerDeliveryCompanyId"),
                                                                selectedSellerDeliveryCompany.deliveryCompany
                                                        )
                                                ).as("deliveryCompanies")
                                        ).as("deliveryCompany"),
                                        GroupBy.list(
                                                Projections.fields(
                                                        SellerDeliveryAddressInfoDTO.class,
                                                        sellerDeliveryAddress.id.as("deliveryAddressId"),
                                                        sellerDeliveryAddress.generalAddress,
                                                        sellerDeliveryAddress.generalDetailAddress,
                                                        sellerDeliveryAddress.generalBusinessName,
                                                        sellerDeliveryAddress.refundAddress,
                                                        sellerDeliveryAddress.refundDetailAddress,
                                                        sellerDeliveryAddress.refundBusinessName,
                                                        sellerDeliveryAddress.refundAccountNumber,
                                                        sellerDeliveryAddress.refundAccountName,
                                                        sellerDeliveryAddress.refundBankCode
                                                )
                                        ).as("deliveryAddress"),
                                        selectedSellerDeliveryAddress.sellerDeliveryAddressId.as("mainDeliveryAddressId")
                                )
                        )
                );

        if (dtos.isEmpty()) {
            return null;
        } else {
            DetailedSellerDTO dto = dtos.get(0);

            // 1. SelectedSellerDeliveryCompanyDTO 중복 제거 및 정렬
            List<SelectedSellerDeliveryCompanyDTO> processedDeliveryCompanies = processDeliveryCompanies(dto.getDeliveryCompany().getDeliveryCompanies());
            dto.getDeliveryCompany().setDeliveryCompanies(processedDeliveryCompanies);

            // 2. SellerDeliveryAddressInfoDTO 중복 제거 및 정렬
            List<SellerDeliveryAddressInfoDTO> processedDeliveryAddresses = processDeliveryAddresses(dto.getDeliveryAddress());
            dto.setDeliveryAddress(processedDeliveryAddresses);

            // 3. manager 사본 데이터 추가
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

            dto.getManager().setBusinessRegistrationUrl(businessRegistration == null ? null : businessRegistration.getAttachFileName());
            dto.getManager().setMailOrderBusinessReportUrl(mailOrderBusinessReport == null ? null : mailOrderBusinessReport.getAttachFileName());

            // 4. adjustment 사본 데이터 추가
            AttachFile bankBookUrl = queryFactory.selectFrom(attachFile)
                    .innerJoin(seller).on(seller.userId.eq(userId))
                    .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                    .where(attachFile.id.eq(sellerAdjustmentInfo.copyBankBookId))
                    .fetchOne();
            dto.getAdjustment().setBankBookUrl(bankBookUrl == null ? null : bankBookUrl.getAttachFileName());

            return dto;
        }
    }

    @Override
    public List<SubCategoryDetailDTO> findAllBrandName() {
        BooleanBuilder sellerBuilder = new BooleanBuilder();
        sellerBuilder.and(seller.sellerType.eq(SellerType.BRAND))
                .and(seller.isDeleted.eq(false));

        return queryFactory
                .select(Projections.constructor(
                        SubCategoryDetailDTO.class,
                        seller.id,
                        seller.marketName,
                        ExpressionUtils.as(
                                JPAExpressions.select(attachFile.attachFileName)
                                        .from(attachFile)
                                        .where(attachFile.id.eq(seller.logoImageId))
                                , "thumbnailUrl"
                        )
                ))
                .from(seller)
                .where(sellerBuilder)
                .fetch();
    }

    /**
     * SellerDeliveryAddressInfoDTO 중복 제거 및 정렬
     *
     * @param deliveryCompanies
     * @return
     */
    private List<SellerDeliveryAddressInfoDTO> processDeliveryAddresses(List<SellerDeliveryAddressInfoDTO> deliveryCompanies) {
        return deliveryCompanies.stream()
                .distinct()
                .sorted(Comparator.comparingLong(SellerDeliveryAddressInfoDTO::getDeliveryAddressId))
                .toList();
    }

    /**
     * SellerDeliveryCompanyInfoDTO 중복 제거 및 정렬
     *
     * @param deliveryCompanies
     * @return
     */
    private List<SelectedSellerDeliveryCompanyDTO> processDeliveryCompanies(List<SelectedSellerDeliveryCompanyDTO> deliveryCompanies) {
        return deliveryCompanies.stream()
                .distinct()
                .sorted(Comparator.comparingLong(SelectedSellerDeliveryCompanyDTO::getSelectedSellerDeliveryCompanyId))
                .toList();
    }

    @Override
    public Page<SellerDTO> getSellers(
            Pageable pageable,
            String brandName,
            String contactName,
            UserStatus status
    ) {
        BooleanBuilder userStatusBuilder = new BooleanBuilder()
                .and(userStatusInfo.userId.eq(seller.userId));
        if (status != null) {
            userStatusBuilder.and(userStatusInfo.status.eq(status));
        }
        BooleanBuilder sellerBuilder = getBooleanBuilderInSellerDTO(brandName, contactName);

        // 1. 전체 데이터 조회
        List<SellerDTO> dtos = getSellerDTOs(userStatusBuilder, sellerBuilder, pageable);

        // 2. 페이징 처리
        Long count = getSellerDTOCount(userStatusBuilder, brandName, contactName);
        return new PageImpl<>(dtos, pageable, count);
    }

    private List<SellerDTO> getSellerDTOs(
            BooleanBuilder userStatusBuilder,
            BooleanBuilder sellerBuilder,
            Pageable pageable
    ) {
        JPAQuery<SellerDTO> query = queryFactory
                .select(Projections.fields(
                        SellerDTO.class,
                        seller.id.as("sellerId"),
                        seller.marketName.as("brandName"),
                        seller.contactName,
                        user.email,
                        user.phoneNumber,
                        seller.entryApprovedAt,
                        user.recentLoginAt
                ))
                .from(seller)
                .innerJoin(user).on(user.id.eq(seller.userId))
                .innerJoin(userStatusInfo).on(userStatusBuilder)
                .where(sellerBuilder);

        if (pageable != null) {
            return query
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            return query.fetch();
        }
    }

    private BooleanBuilder getBooleanBuilderInSellerDTO(
            String brandName,
            String contactName
    ) {
        BooleanBuilder sellerBuilder = new BooleanBuilder();
        sellerBuilder.and(seller.isDeleted.eq(false)
                .and(checkIsContainBrandName(brandName))
                .and(checkIsContainContactName(contactName))
        );

        return sellerBuilder;
    }

    private Long getSellerDTOCount(
            BooleanBuilder userStatusBuilder,
            String brandName,
            String contactName
    ) {
        return queryFactory
                .select(seller.count())
                .from(seller)
                .innerJoin(user).on(user.id.eq(seller.userId))
                .innerJoin(userStatusInfo).on(userStatusBuilder)
                .where(seller.isDeleted.eq(false)
                        .and(checkIsContainBrandName(brandName))
                        .and(checkIsContainContactName(contactName))
                )
                .fetchOne();
    }

    @Override
    public List<SellerDTO> exportSellers(String brandName, String contactName, UserStatus status) {
        BooleanBuilder userStatusBuilder = new BooleanBuilder()
                .and(userStatusInfo.userId.eq(seller.userId));
        if (status != null) {
            userStatusBuilder.and(userStatusInfo.status.eq(status));
        }
        BooleanBuilder sellerBuilder = getBooleanBuilderInSellerDTO(brandName, contactName);

        return getSellerDTOs(
                userStatusBuilder,
                sellerBuilder,
                null
        );
    }

    private BooleanBuilder checkIsContainBrandName(String brandName) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (brandName != null && !brandName.isBlank()) {
            booleanBuilder.and(seller.marketName.containsIgnoreCase(brandName));
        }

        return booleanBuilder;
    }

    private BooleanBuilder checkIsContainContactName(String contactName) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (contactName != null && !contactName.isBlank()) {
            booleanBuilder.and(seller.contactName.containsIgnoreCase(contactName));
        }

        return booleanBuilder;
    }


    @Override
    public SimpleSellerFromAdminDTO getSellerInformation(Long sellerId) {
        SimpleSellerFromAdminDTO dto = queryFactory
                .select(
                        Projections.fields(
                                SimpleSellerFromAdminDTO.class,
                                seller.id,
                                seller.marketName,
                                seller.contactName,
                                user.email,
                                user.phoneNumber,
                                seller.entryApprovedAt,
                                sellerBusinessInfo.representativeContact,
                                sellerBusinessInfo.businessAddress,
                                sellerBusinessInfo.businessRegistrationNumber,
                                sellerBusinessInfo.mailOrderBusinessReportNumber,
                                sellerAdjustmentInfo.bankCode,
                                sellerAdjustmentInfo.accountName,
                                sellerAdjustmentInfo.accountNumber,
                                seller.chargePercent,
                                userStatusInfo.status.as("userStatus")
                        )
                )
                .from(seller)
                .innerJoin(user).on(user.id.eq(seller.userId))
                .leftJoin(userStatusInfo).on(userStatusInfo.userId.eq(seller.userId))
                .leftJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .leftJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(seller.isDeleted.eq(false))
                .fetchFirst();


        // 사본 데이터 추가
        AttachFile businessRegistration = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.id.eq(sellerId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerBusinessInfo.copyBusinessRegistrationCertificateId))
                .fetchOne();

        AttachFile mailOrderBusinessReport = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.id.eq(sellerId))
                .innerJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerBusinessInfo.copyMainOrderBusinessReportCardId))
                .fetchOne();

        dto.setBusinessRegistrationUrl(businessRegistration == null ? null : businessRegistration.getAttachFileName());
        dto.setMailOrderBusinessReportUrl(mailOrderBusinessReport == null ? null : mailOrderBusinessReport.getAttachFileName());

        AttachFile bankBookUrl = queryFactory.selectFrom(attachFile)
                .innerJoin(seller).on(seller.id.eq(sellerId))
                .innerJoin(sellerAdjustmentInfo).on(sellerAdjustmentInfo.sellerId.eq(seller.id))
                .where(attachFile.id.eq(sellerAdjustmentInfo.copyBankBookId))
                .fetchOne();
        dto.setBankBookUrl(bankBookUrl == null ? null : bankBookUrl.getAttachFileName());

        return dto;
    }

    @Override
    public AppSellerDTO getSellerInformationForApp(Long sellerId) {
        return queryFactory
                .select(
                        Projections.fields(
                                AppSellerDTO.class,
                                seller.id.as("sellerId"),
                                seller.marketName,
                                seller.contactName,
                                seller.customerServiceNumber,
                                sellerBusinessInfo.businessEmail,
                                brand.businessDay,
                                brand.openingTime,
                                brand.closingTime,
                                brand.breakingTime
                        )
                )
                .from(seller)
                .leftJoin(sellerBusinessInfo).on(sellerBusinessInfo.sellerId.eq(seller.id))
                .leftJoin(brand).on(brand.sellerId.eq(seller.id))
                .where(seller.id.eq(sellerId))
                .fetchOne();
    }


}
