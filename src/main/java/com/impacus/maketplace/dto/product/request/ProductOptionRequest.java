package com.impacus.maketplace.dto.product.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionRequest {
    private String color;
    private String size;
    private Long stock;
}
