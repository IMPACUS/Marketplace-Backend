package com.impacus.maketplace.controller.order;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.order.request.CreateOrderRequest;
import com.impacus.maketplace.dto.order.response.OrderDTO;
import com.impacus.maketplace.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("")
    public ApiResponseEntity<?> createOrder(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDTO orderDTO = orderService.createOrder(user.getId(), createOrderRequest);
        return ApiResponseEntity
                .builder()
                .data(orderDTO)
                .build();
    }
}
