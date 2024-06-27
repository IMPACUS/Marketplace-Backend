package com.impacus.maketplace.controller.order;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.order.request.OrderReqDTO;
import com.impacus.maketplace.dto.order.response.OrderResDTO;
import com.impacus.maketplace.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ApiResponseEntity<OrderResDTO> createOrder(
            @RequestBody OrderReqDTO orderReqDTO,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        OrderResDTO orderResDto = orderService.createOrder(orderReqDTO, user);
        return ApiResponseEntity.<OrderResDTO>builder()
                .data(orderResDto)
                .build();
    }
}
