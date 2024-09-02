package com.impacus.maketplace.repository.temporaryProduct.querydsl;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProduct;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductDetailInfo;
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
    private final QTemporaryProductDetailInfo temporaryProductDetail = QTemporaryProductDetailInfo.temporaryProductDetailInfo;

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
                .where(temporaryProduct.id.eq(temporaryProductId))
                .execute();
    }

    @Override
    public void updateTemporaryProductDetail(Long temporaryProductId, CreateProductDetailInfoDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);
        if (dto == null) {
            dto = new CreateProductDetailInfoDTO();
        }

        queryFactory
                .update(temporaryProductDetail)
                .set(temporaryProductDetail.productType, dto.getProductType())
                .set(temporaryProductDetail.productMaterial, dto.getProductMaterial())
                .set(temporaryProductDetail.productColor, dto.getProductColor())
                .set(temporaryProductDetail.productSize, dto.getProductSize())
                .set(temporaryProductDetail.dateOfManufacture, dto.getDateOfManufacture())
                .set(temporaryProductDetail.washingPrecautions, dto.getWashingPrecautions())
                .set(temporaryProductDetail.countryOfManufacture, dto.getCountryOfManufacture())
                .set(temporaryProductDetail.manufacturer, dto.getManufacturer())
                .set(temporaryProductDetail.importer, dto.getImporter())
                .set(temporaryProductDetail.qualityAssuranceStandards, dto.getQualityAssuranceStandards())
                .set(temporaryProductDetail.asManager, dto.getManufacturer())
                .set(temporaryProductDetail.contactNumber, dto.getContactNumber())

                .set(temporaryProductDetail.modifyAt, LocalDateTime.now())
                .set(temporaryProductDetail.modifyId, currentAuditor)
                .where(temporaryProductDetail.temporaryProductId.eq(temporaryProductId))
                .execute();
    }

    @Override
    public void updateTemporaryProductAtOptions(Long temporaryProductId, OptionStepProductDTO dto) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
                .update(temporaryProduct)
                .set(temporaryProduct.weight, dto.getWeight())
                .set(temporaryProduct.productStatus, dto.getProductStatus())
                .set(temporaryProduct.description, dto.getDescription())
                .set(temporaryProduct.type, dto.getType())

                .set(temporaryProduct.modifyAt, LocalDateTime.now())
                .set(temporaryProduct.modifyId, currentAuditor)
                .where(temporaryProduct.id.eq(temporaryProductId))
                .execute();
    }
}
