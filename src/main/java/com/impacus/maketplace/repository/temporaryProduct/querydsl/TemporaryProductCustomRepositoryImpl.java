package com.impacus.maketplace.repository.temporaryProduct.querydsl;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.dto.product.response.ProductClaimInfoDTO;
import com.impacus.maketplace.dto.product.response.ProductDeliveryTimeDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailInfoDTO;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProduct;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductClaimInfo;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductDeliveryTime;
import com.impacus.maketplace.entity.temporaryProduct.QTemporaryProductDetailInfo;
import com.querydsl.core.types.Projections;
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
    private final QTemporaryProductClaimInfo claimInfo = QTemporaryProductClaimInfo.temporaryProductClaimInfo;
    private final QTemporaryProductDetailInfo temporaryProductDetail = QTemporaryProductDetailInfo.temporaryProductDetailInfo;
    private final QTemporaryProductDeliveryTime deliveryTime = QTemporaryProductDeliveryTime.temporaryProductDeliveryTime;

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
    public ProductDetailForWebDTO findDetailIdByRegisterId(String registerId) {
        return queryFactory
                .select(
                        Projections.fields(
                                ProductDetailForWebDTO.class,
                                temporaryProduct.id,
                                temporaryProduct.name,
                                temporaryProduct.categoryId,
                                temporaryProduct.deliveryType,
                                temporaryProduct.isCustomProduct,
                                temporaryProduct.deliveryFeeType,
                                temporaryProduct.refundFeeType,
                                temporaryProduct.deliveryFee,
                                temporaryProduct.refundFee,
                                temporaryProduct.specialDeliveryFee,
                                temporaryProduct.specialRefundFee,
                                temporaryProduct.deliveryCompany,
                                temporaryProduct.bundleDeliveryOption,
                                temporaryProduct.bundleDeliveryGroupId,
                                temporaryProduct.salesChargePercent,
                                temporaryProduct.marketPrice,
                                temporaryProduct.appSalesPrice,
                                temporaryProduct.discountPrice,
                                temporaryProduct.weight,
                                temporaryProduct.type,
                                temporaryProduct.productStatus,
                                temporaryProduct.description,
                                Projections.constructor(
                                        ProductDetailInfoDTO.class,
                                        temporaryProductDetail
                                ).as("productDetail"),
                                Projections.constructor(
                                        ProductDeliveryTimeDTO.class,
                                        deliveryTime.minDays,
                                        deliveryTime.maxDays
                                ).as("deliveryTime"),
                                temporaryProduct.productImages,
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
