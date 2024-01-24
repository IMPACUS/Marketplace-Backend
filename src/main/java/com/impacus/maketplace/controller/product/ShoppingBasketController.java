package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import com.impacus.maketplace.dto.shoppingBasket.response.SimpleShoppingBasketDTO;
import com.impacus.maketplace.service.product.ShoppingBasketService;
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
@RequestMapping("api/v1/shopping-basket")
public class ShoppingBasketController {


    private final ShoppingBasketService shoppingBasketService;

    /**
     * 새로운 장바구니 데이터를 추가하는 API
     *
     * @param shoppingBasketRequest
     * @return
     */
    @PostMapping("/user")
    public ApiResponseEntity<Object> addShoppingBasket(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ShoppingBasketRequest shoppingBasketRequest) {
        SimpleShoppingBasketDTO dto = shoppingBasketService.addShoppingBasket(user.getId(), shoppingBasketRequest);
        return ApiResponseEntity
                .builder()
                .data(dto)
                .build();
    }

    // TODO 삭제

    // TODO 수정
}
