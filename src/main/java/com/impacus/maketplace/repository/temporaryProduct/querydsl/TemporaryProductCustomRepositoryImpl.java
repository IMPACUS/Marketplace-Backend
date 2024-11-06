package com.impacus.maketplace.repository.temporaryProduct.querydsl;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductBasicDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProduct;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductClaimInfo;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductDeliveryTime;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductDetailInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
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
    private final QTemporaryProductClaimInfo claimInfo = QTemporaryProductClaimInfo.temporaryProductClaimInfo;
    private final QTemporaryProductDetailInfo temporaryProductDetail = QTemporaryProductDetailInfo.temporaryProductDetailInfo;
    private final QTemporaryProductDeliveryTime deliveryTime = QTemporaryProductDeliveryTime.temporaryProductDeliveryTime;

    @Override
    public void updateTemporaryProduct(Long temporaryProductId, BasicStepProductDTO dto, boolean doesUpdateChargePercent) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        JPAUpdateClause query = queryFactory
                .update(temporaryProduct)
                .set(temporaryProduct.name, dto.getName())
                .set(temporaryProduct.deliveryType, dto.getDeliveryType())
                .set(temporaryProduct.isCustomProduct, dto.getIsCustomProduct())
                .set(temporaryProduct.categoryId, dto.getCategoryId())
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
                .where(temporaryProduct.id.eq(temporaryProductId));

        if (doesUpdateChargePercent) {
            query.set(temporaryProduct.salesChargePercent, dto.getSalesChargePercent());
        }

        query.execute();
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

    @Override
    public void deleteRelationEntityById(Long temporaryProductId) {
        // 배송 지연 시간
        queryFactory
                .delete(deliveryTime)
                .where(deliveryTime.temporaryProductId.eq(temporaryProductId))
                .execute();

        // 상품 상세
        queryFactory
                .delete(temporaryProductDetail)
                .where(temporaryProductDetail.temporaryProductId.eq(temporaryProductId))
                .execute();

        // 상품 클레임 정보
        queryFactory
                .delete(claimInfo)
                .where(claimInfo.temporaryProductId.eq(temporaryProductId))
                .execute();
    }

    @Override
    public TemporaryProductDTO findDetailIdByRegisterId(String registerId) {
        return queryFactory
                .select(
                        Projections.fields(
                                TemporaryProductDTO.class,
                                temporaryProduct.id,
                                Projections.fields(
                                        TemporaryProductBasicDTO.class,
                                        temporaryProduct.name,
                                        temporaryProduct.deliveryType,
                                        temporaryProduct.isCustomProduct,
                                        temporaryProduct.categoryId,
                                        temporaryProduct.deliveryFeeType,
                                        temporaryProduct.refundFeeType,
                                        temporaryProduct.deliveryFee,
                                        temporaryProduct.refundFee,
                                        temporaryProduct.specialDeliveryFee,
                                        temporaryProduct.specialRefundFee,
                                        temporaryProduct.deliveryCompany,
                                        temporaryProduct.bundleDeliveryOption,
                                        temporaryProduct.bundleDeliveryGroupId,
                                        temporaryProduct.productImages,
                                        temporaryProduct.marketPrice,
                                        temporaryProduct.appSalesPrice,
                                        temporaryProduct.discountPrice,
                                        temporaryProduct.salesChargePercent,
                                        Projections.constructor(
                                                ProductDeliveryTimeDTO.class,
                                                deliveryTime.minDays,
                                                deliveryTime.maxDays
                                        ).as("deliveryTime")
                                ).as("information"),
                                Projections.fields(
                                        WebProductSpecificationDTO.class,
                                        temporaryProduct.description,
                                        temporaryProduct.weight,
                                        temporaryProduct.productStatus,
                                        temporaryProduct.type

                                ).as("specification"),
                                Projections.constructor(
                                        ProductDetailInfoDTO.class,
                                        temporaryProductDetail
                                ).as("productDetail"),
                                Projections.constructor(
                                        ProductClaimInfoDTO.class,
                                        claimInfo.recallInfo,
                                        claimInfo.claimCost,
                                        claimInfo.claimPolicyGuild,
                                        claimInfo.claimContactInfo
                                ).as("claim")
                        )
                )
                .from(temporaryProduct)
                .leftJoin(temporaryProductDetail).on(temporaryProductDetail.temporaryProductId.eq(temporaryProduct.id))
                .leftJoin(deliveryTime).on(deliveryTime.temporaryProductId.eq(temporaryProduct.id))
                .leftJoin(claimInfo).on(claimInfo.temporaryProductId.eq(temporaryProduct.id))
                .where(temporaryProduct.registerId.eq(registerId))
                .fetchOne();
    }
}
