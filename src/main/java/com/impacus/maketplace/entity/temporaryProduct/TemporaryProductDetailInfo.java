package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.dto.product.request.CreateProductDetailInfoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_product_detail_info")
public class TemporaryProductDetailInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_detail_info_id")
    private Long id;

    @Column(nullable = false)
    private Long temporaryProductId;

    @Column
    private String productType; // 상품 종류

    @Column
    private String productMaterial; // 상품 재고

    @Column
    private String productColor; // 상품 색상

    @Column
    private String productSize; // 상품 사이즈

    @Column
    private String dateOfManufacture; // 제조일

    @Column
    private String washingPrecautions; // 세탁 시 주의사항

    @Column
    private String countryOfManufacture; // 제조국

    @Column
    private String manufacturer; // 제조자

    @Column
    private String importer; // 수입자

    @Column
    private String qualityAssuranceStandards; // 품질보증기준

    @Column
    private String asManager; // A/S 책임자

    @Column
    private String contactNumber; // 전화번호

    public TemporaryProductDetailInfo(Long productId, CreateProductDetailInfoDTO productDetailInfoRequest) {
        this.temporaryProductId = productId;
        this.productType = productDetailInfoRequest.getProductType();
        this.productMaterial = productDetailInfoRequest.getProductMaterial();
        this.productColor = productDetailInfoRequest.getProductColor();
        this.productSize = productDetailInfoRequest.getProductSize();
        this.dateOfManufacture = productDetailInfoRequest.getDateOfManufacture();
        this.washingPrecautions = productDetailInfoRequest.getWashingPrecautions();
        this.countryOfManufacture = productDetailInfoRequest.getCountryOfManufacture();
        this.manufacturer = productDetailInfoRequest.getManufacturer();
        this.importer = productDetailInfoRequest.getImporter();
        this.qualityAssuranceStandards = productDetailInfoRequest.getQualityAssuranceStandards();
        this.asManager = productDetailInfoRequest.getAsManager();
        this.contactNumber = productDetailInfoRequest.getContactNumber();
    }

    public void setTemporaryProductDetailInfo(CreateProductDetailInfoDTO productDetailInfoRequest) {
        this.productType = productDetailInfoRequest.getProductType();
        this.productMaterial = productDetailInfoRequest.getProductMaterial();
        this.productColor = productDetailInfoRequest.getProductColor();
        this.productSize = productDetailInfoRequest.getProductSize();
        this.dateOfManufacture = productDetailInfoRequest.getDateOfManufacture();
        this.washingPrecautions = productDetailInfoRequest.getWashingPrecautions();
        this.countryOfManufacture = productDetailInfoRequest.getCountryOfManufacture();
        this.manufacturer = productDetailInfoRequest.getManufacturer();
        this.importer = productDetailInfoRequest.getImporter();
        this.qualityAssuranceStandards = productDetailInfoRequest.getQualityAssuranceStandards();
        this.asManager = productDetailInfoRequest.getAsManager();
        this.contactNumber = productDetailInfoRequest.getContactNumber();
    }
}
