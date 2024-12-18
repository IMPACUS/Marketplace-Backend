package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebProductBasicDTO {
    private String name;
    private DeliveryType deliveryType;
    private Boolean isCustomProduct;

    @Setter
    private Long categoryId;
    private DeliveryRefundType deliveryFeeType;
    private DeliveryRefundType refundFeeType;
    private Integer deliveryFee;
    private Integer refundFee;
    private Integer specialDeliveryFee;
    private Integer specialRefundFee;
    private DeliveryCompany deliveryCompany;
    private BundleDeliveryOption bundleDeliveryOption;

    @Setter
    private Long bundleDeliveryGroupId;

    private List<String> productImages;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer marketPrice = 0;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer appSalesPrice = 0;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer discountPrice = 0;

    private Integer salesChargePercent;

    @Setter
    private ProductDeliveryTimeDTO deliveryTime;
}
