package com.impacus.maketplace.dto.order.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderCheckoutCartDTO {
    String orderNumber;
    List<CheckoutProductDTO> products;

    public OrderCheckoutCartDTO(String orderNumber, List<CheckoutProductDTO> products) {
        this.orderNumber = orderNumber;
        this.products = products;
    }
}
