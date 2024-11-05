package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WebProductBasicDTO {
    private String name;
    private DeliveryType deliveryType;
    private Boolean isCustomProduct;
    private Long categoryId;
    private DeliveryRefundType deliveryFeeType;
    private DeliveryRefundType refundFeeType;
    private Integer deliveryFee;
    private Integer refundFee;
    private Integer specialDeliveryFee;
    private Integer specialRefundFee;
    private DeliveryCompany deliveryCompany;
    private BundleDeliveryOption bundleDeliveryOption;
    private Long bundleDeliveryGroupId;
    private List<String> productImages;
    private int marketPrice;
    private int appSalesPrice;
    private int discountPrice;
    private Integer salesChargePercent;
    private ProductDeliveryTimeDTO deliveryTime;
}
