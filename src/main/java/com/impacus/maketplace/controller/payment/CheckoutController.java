package com.impacus.maketplace.controller.payment;


import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.payment.request.CheckoutCartDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutCartProductsDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutProductDTO;
import com.impacus.maketplace.dto.payment.response.PaymentCartDTO;
import com.impacus.maketplace.dto.payment.response.PaymentSingleDTO;
import com.impacus.maketplace.service.payment.checkout.CheckoutService;
import jakarta.validation.Valid;
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
    public ApiResponseEntity<CheckoutCartProductsDTO> getCheckoutCart(@RequestParam(name = "shopping-basket-id-list") List<Long> shoppingBasketIdList) {

        List<CheckoutProductDTO> products = checkoutService.getCheckoutCart(shoppingBasketIdList);
        CheckoutCartProductsDTO response = CheckoutCartProductsDTO.builder()
                .products(products)
                .shoppingBasketIdList(shoppingBasketIdList)
                .build();

        return ApiResponseEntity
                .<CheckoutCartProductsDTO>builder()
                .data(response)
                .build();
    }

    /**
     * 결제하기 처리 (단일 상품 구매) - 전처리 단계
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("checkout-single")
    public ApiResponseEntity<PaymentSingleDTO> checkoutSingle(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CheckoutSingleDTO checkoutSingleDTO) {
        PaymentSingleDTO response = checkoutService.checkoutSingle(user.getId(), checkoutSingleDTO);

        return ApiResponseEntity
                .<PaymentSingleDTO>builder()
                .data(response)
                .build();
    }

    /**
     * 결제하기 처리 (장바구니 구매) - 전처리 단계
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("checkout-cart")
    public ApiResponseEntity<PaymentCartDTO> checkoutCart(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody CheckoutCartDTO checkoutCartDTO) {

        PaymentCartDTO response = checkoutService.checkoutCart(user.getId(), checkoutCartDTO);

        return ApiResponseEntity
                .<PaymentCartDTO>builder()
                .data(response)
                .build();
    }
}
