package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.product.ProductDetailInfo;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDetailInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDetailInfoDTO {
    @NotBlank
    private String productType; // 상품 종류
    @NotBlank
    private String productMaterial; // 상품 재료
    @NotBlank
    private String productColor; // 상품 색상
    @NotBlank
    private String productSize; // 상품 사이즈
    @NotBlank
    private String dateOfManufacture; // 제조일
    @NotBlank
    private String washingPrecautions; // 세탁 시 주의사항
    @NotBlank
    private String countryOfManufacture; // 제조국
    @NotBlank
    private String manufacturer; // 제조자
    @NotBlank
    private String importer; // 수입자
    @NotBlank
    private String qualityAssuranceStandards; // 품질보증기준
    @NotBlank
    private String asManager; // A/S 책임자
    @NotBlank
    private String contactNumber; // 전화번호

    public ProductDetailInfo toEntity(Long productId) {
        return new ProductDetailInfo(productId, this);
    }

    public TemporaryProductDetailInfo toTemporaryEntity(Long temporaryProductId) {
        return new TemporaryProductDetailInfo(temporaryProductId, this);
    }
}
