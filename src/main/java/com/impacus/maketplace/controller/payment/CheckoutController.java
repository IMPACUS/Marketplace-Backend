package com.impacus.maketplace.controller.payment;


import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutProductDTO;
import com.impacus.maketplace.service.payment.PaymentService;
import com.impacus.maketplace.service.payment.checkout.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class CheckoutController {

    private final CheckoutService checkoutService;

    /**
     * 단일 상품 결제 페이지 이동
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("checkout-single")
    public ApiResponseEntity<CheckoutProductDTO> getCheckoutSingle(@RequestParam(name = "product-id") Long productId,
                                                                   @RequestParam(name = "product-option-id") Long productOptionId,
                                                                   @RequestParam(name = "quantity") Long quantity) {

        CheckoutProductDTO response = checkoutService.getCheckoutSingle(productId, productOptionId, quantity);

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
    public ApiResponseEntity<List<CheckoutProductDTO>> getCheckoutCart(@RequestParam(name = "shopping-basket-id-list") List<Long> shoppingBasketIdList) {

        List<CheckoutProductDTO> response = checkoutService.getCheckoutCart(shoppingBasketIdList);

        return ApiResponseEntity
                .<List<CheckoutProductDTO>>builder()
                .data(response)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("checkout-single")
    public ApiResponseEntity<Void> checkoutSingle(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CheckoutSingleDTO checkoutSingleDTO) {
        checkoutService.checkoutSingle(user.getId(), checkoutSingleDTO);

        return ApiResponseEntity
                .<Void>builder()
                .data(null)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("checkout-cart")
    public ApiResponseEntity<Void> checkoutCart() {

        return null;
    }
}
