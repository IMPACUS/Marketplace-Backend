package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDetailInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailInfoDTO {

    private String productType; // 상품 종류

    private String productMaterial; // 상품 재고

    private String productColor; // 상품 색상

    private String productSize; // 상품 사이즈

    private String dateOfManufacture; // 제조일

    private String washingPrecautions; // 세탁 시 주의사항

    private String countryOfManufacture; // 제조국

    private String manufacturer; // 제조자

    private String importer; // 수입자

    private String qualityAssuranceStandards; // 품질보증기준

    private String asManager; // A/S 책임자

    private String contactNumber; // 전화번호

    public ProductDetailInfoDTO(ProductDetailInfo productDetailInfo) {
        this.productType = productDetailInfo.getProductType();
        this.productMaterial = productDetailInfo.getProductMaterial();
        this.productColor = productDetailInfo.getProductColor();
        this.productSize = productDetailInfo.getProductSize();
        this.dateOfManufacture = productDetailInfo.getDateOfManufacture();
        this.washingPrecautions = productDetailInfo.getWashingPrecautions();
        this.countryOfManufacture = productDetailInfo.getCountryOfManufacture();
        this.manufacturer = productDetailInfo.getManufacturer();
        this.importer = productDetailInfo.getImporter();
        this.qualityAssuranceStandards = productDetailInfo.getQualityAssuranceStandards();
        this.asManager = productDetailInfo.getAsManager();
        this.contactNumber = productDetailInfo.getContactNumber();
    }

    public ProductDetailInfoDTO(TemporaryProductDetailInfo productDetailInfo) {
        this.productType = productDetailInfo.getProductType();
        this.productMaterial = productDetailInfo.getProductMaterial();
        this.productColor = productDetailInfo.getProductColor();
        this.productSize = productDetailInfo.getProductSize();
        this.dateOfManufacture = productDetailInfo.getDateOfManufacture();
        this.washingPrecautions = productDetailInfo.getWashingPrecautions();
        this.countryOfManufacture = productDetailInfo.getCountryOfManufacture();
        this.manufacturer = productDetailInfo.getManufacturer();
        this.importer = productDetailInfo.getImporter();
        this.qualityAssuranceStandards = productDetailInfo.getQualityAssuranceStandards();
        this.asManager = productDetailInfo.getAsManager();
        this.contactNumber = productDetailInfo.getContactNumber();
    }

    @JsonIgnore
    public boolean isNull() {
        return productType == null &&
                productMaterial== null &&
                productColor== null &&
                productSize== null &&
                dateOfManufacture == null &&
                washingPrecautions == null &&
                countryOfManufacture == null &&
                manufacturer == null &&
                importer== null &&
                qualityAssuranceStandards == null &&
                asManager == null &&
                contactNumber == null;

    }
}
