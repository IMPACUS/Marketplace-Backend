package com.impacus.maketplace.dto.temporaryProduct.response;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.ProductClaimInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemporaryProductDTO {
    private String name;
    private DeliveryType deliveryType;
    private DeliveryCompany deliveryCompany;
    private Long categoryId;
    private DeliveryRefundType deliveryFeeType;
    private DeliveryRefundType refundFeeType;
    private Integer deliveryFee;
    private Integer refundFee;
    private Integer specialDeliveryFee;
    private Integer specialRefundFee;
    private int marketPrice;
    private int appSalesPrice;
    private int discountPrice;
    private int weight;
    private ProductStatus productStatus;
    private ProductType type;
    private String description;
    private TemporaryDetailInfoDTO productDetail;
    private List<TemporaryProductOptionDTO> productOptions;
    private List<AttachFileDTO> productImageList;
    private TemporaryProductDeliveryTimeDTO deliveryTime;
    private ProductClaimInfoDTO claim;

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

    public void setClaim(ProductClaimInfoDTO claim) {
        this.claim = claim;
    }
}
