package com.impacus.maketplace.dto.order.response;

import lombok.Data;

@Data
public class OrderCheckoutProductDTO {
    String orderNumber;
    CheckoutProductDTO product;

    public OrderCheckoutProductDTO(String orderNumber, CheckoutProductDTO product) {
        this.orderNumber = orderNumber;
        this.product = product;
    }
}
