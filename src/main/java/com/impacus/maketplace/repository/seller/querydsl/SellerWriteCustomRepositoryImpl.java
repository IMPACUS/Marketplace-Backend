package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;
import com.impacus.maketplace.entity.seller.QBrand;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.QSellerBusinessInfo;
import com.impacus.maketplace.entity.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerWriteCustomRepositoryImpl implements SellerWriteCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QSeller seller = QSeller.seller;
    private final QUser user = QUser.user;
    private final QSellerBusinessInfo sellerBusinessInfo = QSellerBusinessInfo.sellerBusinessInfo;
    private final QBrand brand = QBrand.brand;

    @Override
    public void updateBrandInformationByUserId(
            Long userId,
            Long sellerId,
            ChangeBrandInfoDTO dto,
            boolean isExistedBrand
    ) {

        // seller 데이터 업데이트
        queryFactory
                .update(seller)
                .set(seller.marketName, dto.getBrandName())
                .set(seller.customerServiceNumber, dto.getCustomerServiceNumber())
                .where(seller.userId.eq(userId))
                .execute();

        // seller_business_info 데이터 업데이트
        queryFactory
                .update(sellerBusinessInfo)
                .set(sellerBusinessInfo.businessEmail, dto.getRepresentativeName())
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
                    .where(brand.sellerId.eq(sellerId))
                    .execute();
        }
    }
}
