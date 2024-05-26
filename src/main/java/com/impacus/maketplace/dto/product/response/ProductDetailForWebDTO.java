package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailForWebDTO {
    private String name;
    private String description;
    private DeliveryType deliveryType;
    private Long categoryId;
    private int deliveryFee;
    private int refundFee;
    private int marketPrice;
    private int appSalesPrice;
    private int discountPrice;
    private int weight;
    private ProductType type;
    private ProductDetailInfoDTO productDetail;
    private List<ProductOptionDTO> productOptions;
    private ProductStatus productStatus;
    private List<AttachFileDTO> productImageList;
    private ProductDeliveryTimeDTO deliveryTime;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductOptionDTO(List<ProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }

    public void setDeliveryTime(ProductDeliveryTimeDTO deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
