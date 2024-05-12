package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketDTO;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketForQuantityDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.SimpleShoppingBasketDTO;
import com.impacus.maketplace.service.product.ShoppingBasketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

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
            @Valid @RequestBody ShoppingBasketDTO shoppingBasketRequest) {
        SimpleShoppingBasketDTO dto = shoppingBasketService.addShoppingBasket(user.getId(), shoppingBasketRequest);
        return ApiResponseEntity
                .builder()
                .data(dto)
                .build();
    }

    /**
     * 장바구니 데이터들를 삭제하는 API
     *
     * @param shoppingBasketList
     * @return
     */
    @DeleteMapping("/user")
    public ApiResponseEntity<Object> deleteShoppingBasket(@RequestParam(name = "shopping-basket-id") List<Long> shoppingBasketList) {
        shoppingBasketService.deleteAllShoppingBasket(shoppingBasketList);
        return ApiResponseEntity
                .builder()
                .build();
    }

    /**
     * 장바구니 수량을 수정하는 API
     *
     * @param shoppingBasketRequest
     * @return
     */
    @PutMapping("/user/{shoppingBasketId}")
    public ApiResponseEntity<Object> updateShoppingBasket(
            @PathVariable(name = "shoppingBasketId") Long shoppingBasketId,
            @Valid @RequestBody ShoppingBasketForQuantityDTO shoppingBasketRequest) {
        SimpleShoppingBasketDTO dto = shoppingBasketService.updateShoppingBasket(shoppingBasketId, shoppingBasketRequest);
        return ApiResponseEntity
                .builder()
                .data(dto)
                .build();
    }

    /**
     * 장바구니 데이터 조회 API
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    public ApiResponseEntity<Object> getShoppingBasket(@AuthenticationPrincipal CustomUserDetails user) {
        List<ShoppingBasketDetailDTO> shoppingBasketDetailDTOS = shoppingBasketService.getAllShoppingBasket(user.getId());
        return ApiResponseEntity
                .builder()
                .data(shoppingBasketDetailDTOS)
                .build();
    }
}
