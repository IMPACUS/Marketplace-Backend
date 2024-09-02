package com.impacus.maketplace.repository.temporaryProduct.querydsl;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TemporaryProductCustomRepositoryImpl implements TemporaryProductCustomRepository{
    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QTemporaryProduct temporaryProduct = QTemporaryProduct.temporaryProduct;

    @Override
    public void updateTemporaryProduct(Long temporaryProductId, BasicStepProductDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
                .update(temporaryProduct)
                .set(temporaryProduct.name, dto.getName())
                .set(temporaryProduct.deliveryType, dto.getDeliveryType())
                .set(temporaryProduct.isCustomProduct, dto.getIsCustomProduct())
                .set(temporaryProduct.deliveryFeeType, dto.getDeliveryFeeType())
                .set(temporaryProduct.refundFeeType, dto.getRefundFeeType())
                .set(temporaryProduct.deliveryFee, dto.getDeliveryFee())
                .set(temporaryProduct.refundFee, dto.getRefundFee())
                .set(temporaryProduct.specialDeliveryFee,dto.getSpecialDeliveryFee())
                .set(temporaryProduct.specialRefundFee, dto.getSpecialRefundFee())
                .set(temporaryProduct.deliveryCompany, dto.getDeliveryCompany())
                .set(temporaryProduct.bundleDeliveryOption, dto.getBundleDeliveryOption())
                .set(temporaryProduct.bundleDeliveryGroupId, dto.getBundleDeliveryGroupId())
                .set(temporaryProduct.productImages, dto.getProductImages())
                .set(temporaryProduct.marketPrice, dto.getMarketPrice())
                .set(temporaryProduct.appSalesPrice, dto.getAppSalesPrice())
                .set(temporaryProduct.discountPrice, dto.getDiscountPrice())

                .set(temporaryProduct.modifyAt, LocalDateTime.now())
                .set(temporaryProduct.modifyId, currentAuditor)
                .execute();
    }
}
