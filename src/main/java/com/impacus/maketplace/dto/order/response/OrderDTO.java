package com.impacus.maketplace.dto.order.response;

import com.impacus.maketplace.common.enumType.OrderStatus;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long shoppingBasketId;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    public static OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .shoppingBasketId(order.getShoppingBasketId())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }
}
