package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long brandId;

    private String name;

    private String description;

    private DeliveryType deliveryType;

    private SubCategory categoryType;

    private int deliveryFee;

    private int refundFee;

    private int marketPrice;

    private int appSalesPrice;

    private int discountPrice;
    
    private int weight;

    private ProductDetailInfoRequest productDetail;

    private List<ProductOptionRequest> productOptions;
}
