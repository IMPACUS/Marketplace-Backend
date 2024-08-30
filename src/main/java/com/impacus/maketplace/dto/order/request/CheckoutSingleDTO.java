package com.impacus.maketplace.dto.order.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckoutSingleDTO {
    private Long productId;
    private Long count;
    private Long productOptionId;
}
