package com.impacus.maketplace.dto.payment.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckoutCartProductsDTO {
    private List<CheckoutProductDTO> products;
    private List<Long> shoppingBasketIdList;
}
