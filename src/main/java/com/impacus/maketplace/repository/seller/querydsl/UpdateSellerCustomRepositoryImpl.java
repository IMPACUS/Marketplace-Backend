package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.*;
import com.impacus.maketplace.dto.user.PhoneNumberDTO;
import com.impacus.maketplace.entity.seller.QBrand;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.QSellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.QSellerBusinessInfo;
import com.impacus.maketplace.entity.seller.delivery.QSellerDeliveryAddress;
import com.impacus.maketplace.entity.seller.deliveryCompany.QSellerDeliveryCompany;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.entity.user.QUserStatusInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UpdateSellerCustomRepositoryImpl implements UpdateSellerCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QSeller seller = QSeller.seller;
    private final QSellerBusinessInfo sellerBusinessInfo = QSellerBusinessInfo.sellerBusinessInfo;
    private final QBrand brand = QBrand.brand;
    private final QSellerAdjustmentInfo sellerAdjustmentInfo = QSellerAdjustmentInfo.sellerAdjustmentInfo;
    private final QUser user = QUser.user;
    private final QSellerDeliveryCompany sellerDeliveryCompany = QSellerDeliveryCompany.sellerDeliveryCompany;
    private final QSellerDeliveryAddress sellerDeliveryAddress = QSellerDeliveryAddress.sellerDeliveryAddress;
    private final QUserStatusInfo userStatusInfo = QUserStatusInfo.userStatusInfo;

    @Override
    public void updateBrandInformationByUserId(
            Long userId,
            Long sellerId,
            ChangeBrandInfoDTO dto,
            boolean isExistedBrand
    ) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // seller 데이터 업데이트
        queryFactory
                .update(seller)
                .set(seller.marketName, dto.getBrandName())
                .set(seller.customerServiceNumber, dto.getCustomerServiceNumber())
                .set(seller.modifyAt, LocalDateTime.now())
                .set(seller.modifyId, currentAuditor)
                .where(seller.userId.eq(userId))
                .execute();

        // seller_business_info 데이터 업데이트
        queryFactory
                .update(sellerBusinessInfo)
                .set(sellerBusinessInfo.businessEmail, dto.getRepresentativeName())
                .set(sellerBusinessInfo.modifyAt, LocalDateTime.now())
                .set(sellerBusinessInfo.modifyId, currentAuditor)
                .where(sellerBusinessInfo.sellerId.eq(sellerId))
                .execute();

        // seller_brand_info 데이터 업데이트
        if (isExistedBrand) {
            queryFactory
                    .update(brand)
                    .set(brand.introduction, dto.getBrandIntroduction())
                    .set(brand.openingTime, dto.getOpeningTime())
                    .set(brand.closingTime, dto.getClosingTime())
                    .set(brand.businessDay, dto.getBusinessDay())
                    .set(brand.breakingTime, dto.getBreakingTime())
                    .set(brand.modifyAt, LocalDateTime.now())
                    .set(brand.modifyId, currentAuditor)
                    .where(brand.sellerId.eq(sellerId))
                    .execute();
        }
    }

    @Override
    public void updateManagerInformationBySellerId(Long sellerId, ChangeSellerManagerInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // seller_business_info 데이터 업데이트
        queryFactory
                .update(sellerBusinessInfo)
                .set(sellerBusinessInfo.representativeName, dto.getRepresentativeName())
                .set(sellerBusinessInfo.businessAddress, dto.getAddress())
                .set(sellerBusinessInfo.businessRegistrationNumber, dto.getBusinessRegistrationNumber())
                .set(sellerBusinessInfo.mailOrderBusinessReportNumber, dto.getMailOrderBusinessReportNumber())
                .set(sellerBusinessInfo.modifyAt, LocalDateTime.now())
                .set(sellerBusinessInfo.modifyId, currentAuditor)
                .where(sellerBusinessInfo.sellerId.eq(sellerId))
                .execute();
    }

    @Override
    public void updateAdjustmentInformationBySellerId(Long sellerId, ChangeSellerAdjustmentInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // seller_adjustment_info 데이터 업데이트
        queryFactory
                .update(sellerAdjustmentInfo)
                .set(sellerAdjustmentInfo.bankCode, dto.getBankCode())
                .set(sellerAdjustmentInfo.accountName, dto.getAccountName())
                .set(sellerAdjustmentInfo.accountNumber, dto.getAccountNumber())
                .set(sellerAdjustmentInfo.modifyAt, LocalDateTime.now())
                .set(sellerAdjustmentInfo.modifyId, currentAuditor)
                .where(sellerAdjustmentInfo.sellerId.eq(sellerId))
                .execute();
    }

    @Override
    public void updateLoginInformationByUserId(Long userId, ChangeSellerLoginInfoDTO dto, String encodedPassword) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);
        PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(dto.getPhoneNumber());

        // 로그인 정보 변경
        queryFactory
                .update(user)
                .set(user.email, dto.getEmail())
                .set(user.password, encodedPassword)
                .set(user.phoneNumberPrefix, phoneNumberDTO.getPhoneNumberPrefix())
                .set(user.phoneNumberSuffix, phoneNumberDTO.getPhoneNumberSuffix())
                .set(user.modifyAt, LocalDateTime.now())
                .set(user.modifyId, currentAuditor)
                .where(user.id.eq(userId))
                .execute();
    }

    @Override
    public void updateDeliveryCompanyInformationBySellerId(Long sellerId, ChangeSellerDeliveryCompanyInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 택배사 정보 변경
        queryFactory
                .update(sellerDeliveryCompany)
                .set(sellerDeliveryCompany.generalDeliveryFee, dto.getGeneralDeliveryFee())
                .set(sellerDeliveryCompany.generalSpecialDeliveryFee, dto.getGeneralSpecialDeliveryFee())
                .set(sellerDeliveryCompany.refundDeliveryFee, dto.getRefundDeliveryFee())
                .set(sellerDeliveryCompany.refundSpecialDeliveryFee, dto.getGeneralSpecialDeliveryFee())
                .set(sellerDeliveryCompany.modifyAt, LocalDateTime.now())
                .set(sellerDeliveryCompany.modifyId, currentAuditor)
                .where(sellerDeliveryCompany.sellerId.eq(sellerId))
                .execute();
    }

    @Override
    public Long updateDeliveryAddressInformationBySellerIdAndId(Long sellerId, ChangeSellerDeliveryAddressInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(sellerDeliveryAddress.sellerId.eq(sellerId))
                .and(sellerDeliveryAddress.id.eq(dto.getDeliveryAddressId()));

        // 배송지 정보 변경
        return queryFactory
                .update(sellerDeliveryAddress)
                .set(sellerDeliveryAddress.generalAddress, dto.getGeneralAddress())
                .set(sellerDeliveryAddress.generalDetailAddress, dto.getGeneralDetailAddress())
                .set(sellerDeliveryAddress.generalBusinessName, dto.getGeneralBusinessName())
                .set(sellerDeliveryAddress.refundAddress, dto.getRefundAddress())
                .set(sellerDeliveryAddress.refundDetailAddress, dto.getRefundDetailAddress())
                .set(sellerDeliveryAddress.refundBusinessName, dto.getRefundBusinessName())
                .set(sellerDeliveryAddress.refundAccountNumber, dto.getRefundAccountNumber())
                .set(sellerDeliveryAddress.refundAccountName, dto.getRefundAccountName())
                .set(sellerDeliveryAddress.refundBankCode, dto.getRefundBankCode())


                .set(sellerDeliveryAddress.modifyAt, LocalDateTime.now())
                .set(sellerDeliveryAddress.modifyId, currentAuditor)
                .where(builder)
                .execute();
    }

    @Override
    public void updateSellerInformation(
            Long userId,
            Long sellerId,
            UpdateSellerInfoFromAdminDTO dto,
            Long profileImageId
    ) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // userStatus 정보 업데이트
        queryFactory
                .update(userStatusInfo)
                .set(userStatusInfo.status, dto.getUserStatus())

                .set(userStatusInfo.modifyAt, LocalDateTime.now())
                .set(userStatusInfo.modifyId, currentAuditor)
                .where(userStatusInfo.userId.eq(userId))
                .execute();

        // 판매자 정보 업데이트
        queryFactory
                .update(seller)
                .set(seller.chargePercent, dto.getCharge())
                .set(seller.logoImageId, profileImageId)

                .set(seller.modifyAt, LocalDateTime.now())
                .set(seller.modifyId, currentAuditor)
                .where(seller.id.eq(sellerId))
                .execute();
    }
}
