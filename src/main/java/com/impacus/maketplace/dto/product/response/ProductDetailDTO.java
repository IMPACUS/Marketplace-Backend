package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDTO {
    private Long id;
    private String name;
    private List<AttachFileDTO> productImageList;
    private float averageRating; // 평균 평점
    private Long reviewCnt;
    private int appSalePrice; // 판매가
    private int discountPrice; // 할인가
    private int discountRate; // 할인률
    private List<ProductOptionDTO> options;

    @QueryProjection
    public ProductDetailDTO(Long id,
                            String name,
                            int appSalePrice,
                            int discountPrice,
                            List<ProductOptionDTO> options) {
        this.id = id;
        this.name = name;
        this.appSalePrice = appSalePrice;
        this.discountPrice = discountPrice;
        this.options = options;

    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }
}
