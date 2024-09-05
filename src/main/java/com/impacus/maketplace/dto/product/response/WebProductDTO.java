package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.entity.product.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebProductDTO {
    private Long productId;
    private String productNumber;
    private String name;
    private String totalOptionSize;
    private String totalOptionColor;
    private List<String> productImages;
    private ProductStatus productStatus;
    private long wishlistCnt;
    private int reviewCnt;
    private Integer appSalesPrice;
    private Integer discountPrice;
    private int orderCnt;
    private List<ProductOptionDTO> options;

    public WebProductDTO(
        Long productId,
        String productNumber,
        String name,
        List<String> productImages,
        ProductStatus productStatus,
        long wishlistCnt,
        Integer appSalesPrice,
        Integer discountPrice,
        List<ProductOptionDTO> options
    ) {
        this.productId = productId;
        this.productNumber = productNumber;
        this.name = name;
        this.productImages = productImages;
        this.productStatus = productStatus;
        this.wishlistCnt = wishlistCnt;
        this.appSalesPrice = appSalesPrice;
        this.discountPrice = discountPrice;
        this.options = options;

        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        for (ProductOptionDTO option : options) {
            sizes.add(option.getSize());
            colors.add(option.getColor());
        }

        this.totalOptionSize = String.join(", ", sizes);
        this.totalOptionColor = String.join(", ", colors);
    }
}
