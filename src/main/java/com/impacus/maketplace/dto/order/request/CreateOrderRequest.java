package com.impacus.maketplace.dto.order.request;

import com.impacus.maketplace.common.enumType.OrderStatus;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long shoppingBasketId;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    public static Order toEntity(CreateOrderRequest request, Long userId) {
        return Order.builder()
                .shoppingBasketId(request.getShoppingBasketId())
                .orderStatus(request.getOrderStatus())
                .paymentMethod(request.getPaymentMethod())
                .userId(userId)
                .build();
    }
}
