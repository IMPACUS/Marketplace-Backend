package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.dto.product.request.ProductDetailInfoRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_detail_info")
@SQLDelete(sql = "UPDATE product_detail_info SET is_deleted = true WHERE product_detail_info_id = ?")
@Where(clause = "is_deleted = false")
public class ProductDetailInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_info_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productType; // 상품 종류

    @Column(nullable = false)
    private String productMaterial; // 상품 재고

    @Column(nullable = false)
    private String productColor; // 상품 색상

    @Column(nullable = false)
    private String productSize; // 상품 사이즈

    @Column(nullable = false)
    private String dateOfManufacture; // 제조일

    @Column(nullable = false)
    private String washingPrecautions; // 세탁 시 주의사항

    @Column(nullable = false)
    private String countryOfManufacture; // 제조국

    @Column(nullable = false)
    private String manufacturer; // 제조자

    @Column(nullable = false)
    private String importer; // 수입자

    @Column(nullable = false)
    private String qualityAssuranceStandards; // 품질보증기준

    @Column(nullable = false)
    private String asManager; // A/S 책임자

    @Column(nullable = false)
    private String contactNumber; // 전화번호

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    public ProductDetailInfo(Long productId, ProductDetailInfoRequest productDetailInfoRequest) {
        this.productId = productId;
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
