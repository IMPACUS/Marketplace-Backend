package com.impacus.maketplace.controller.order;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.order.response.CheckoutProductDTO;
import com.impacus.maketplace.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    /**
     * 단일 상품 결제 페이지 이동
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("checkout_single")
    public ApiResponseEntity<CheckoutProductDTO> getCheckoutSingle(@RequestParam(name = "product-id") Long productId,
                                                                   @RequestParam(name = "product-option-id") Long productOptionId,
                                                                   @RequestParam(name = "quantity") Long quantity) {

        CheckoutProductDTO response = orderService.getCheckoutSingle(productId, productOptionId, quantity);

        return ApiResponseEntity
                .<CheckoutProductDTO>builder()
                .data(response)
                .build();
    }

    /**
     * 장바구니 결제 페이지로 이동
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("checkout-cart")
    public ApiResponseEntity<Void> getCheckoutCart(@AuthenticationPrincipal CustomUserDetails user,
                                                   @RequestParam(name = "check-product-id-list") List<Long> productIdList) {

        orderService.getCheckoutCart(user.getId(), productIdList);
        return null;
    }
}
