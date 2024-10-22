package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.utils.ProductUtils;
import com.impacus.maketplace.entity.product.ProductOption;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebProductTableDTO {
    private Long productId;
    private String name;
    private List<String> productImages;
    private String productNumber;
    private DeliveryType deliveryType;
    private long orderCnt;
    private LocalDateTime createAt;
    private List<WebProductOptionDTO> options;
    private Integer deliveryFee;

    @QueryProjection
    public WebProductTableDTO(
        Long id,
        String name,
        List<String> productImages,
        String productNumber,
        DeliveryType deliveryType,
        LocalDateTime createAt,
        List<ProductOption> productOptions,
        int deliveryFee,
        DeliveryRefundType deliveryFeeType,
        Integer sellerDeliveryFee

    ) {
        this.productId = id;
        this.name = name;
        this.productImages = productImages;
        this.productNumber = productNumber;
        this.deliveryType = deliveryType;
        this.createAt = createAt;
        List<WebProductOptionDTO> options = new ArrayList<>();
        for (ProductOption productOption : productOptions) {
            options.add(new WebProductOptionDTO(productOption.getId(), productOption.getColor(), productOption.getSize()));
        }
        this.options = options;
        this.deliveryFee = ProductUtils.getProductDeliveryFee(deliveryFee, deliveryFeeType, sellerDeliveryFee);
    }

}
