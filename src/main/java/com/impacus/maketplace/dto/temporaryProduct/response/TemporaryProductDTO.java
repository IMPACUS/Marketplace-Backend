package com.impacus.maketplace.dto.temporaryProduct.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemporaryProductDTO {
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
    private TemporaryDetailInfoDTO productDetail;
    private List<TemporaryProductOptionDTO> productOptions;
    private ProductStatus productStatus;
    private List<AttachFileDTO> productImageList;
    private TemporaryProductDeliveryTimeDTO deliveryTime;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemporaryProductOptionDTO(List<TemporaryProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }

    public void setTemporaryDetailInfoDTO(TemporaryDetailInfoDTO productDetail) {
        this.productDetail = productDetail;
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }

    public void setDeliveryTime(TemporaryProductDeliveryTimeDTO deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
