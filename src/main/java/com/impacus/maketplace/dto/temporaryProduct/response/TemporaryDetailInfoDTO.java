package com.impacus.maketplace.dto.temporaryProduct.response;

import lombok.Data;

@Data
public class TemporaryDetailInfoDTO {
    private String productType; // 상품 종류
    private String productMaterial; // 상품 재료
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
}
