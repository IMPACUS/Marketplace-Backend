package com.impacus.maketplace.dto.bundleDelivery.response;

import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import com.impacus.maketplace.dto.product.response.WebProductOptionDTO;
import com.impacus.maketplace.entity.product.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BundleDeliveryGroupProductDTO {
    private Long productId;
    private String groupNumber;
    private String productNumber;
    private String name;
    private List<String> productImages;
    private DeliveryFeeRule deliveryFeeRule;
    private LocalDateTime bundleDeliveryOptionAppliedAt;
    private List<WebProductOptionDTO> options;

    public BundleDeliveryGroupProductDTO(
            Long productId,
            String groupNumber,
            String productNumber,
            String name,
            List<String> productImages,
            DeliveryFeeRule deliveryFeeRule,
            LocalDateTime bundleDeliveryOptionAppliedAt,
            List<ProductOption> productOptions
    ) {
        this.productId = productId;
        this.groupNumber = groupNumber;
        this.productNumber = productNumber;
        this.name = name;
        this.productImages = productImages;
        this.deliveryFeeRule = deliveryFeeRule;
        this.bundleDeliveryOptionAppliedAt = bundleDeliveryOptionAppliedAt;
        List<WebProductOptionDTO> options = new ArrayList<>();
        for (ProductOption productOption : productOptions) {
            options.add(new WebProductOptionDTO(productOption.getId(), productOption.getColor(), productOption.getSize()));
        }

        this.options = options;
    }
}
