package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.shoppingBasket.request.ChangeShoppingBasketQuantityDTO;
import com.impacus.maketplace.dto.shoppingBasket.request.CreateShoppingBasketDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import com.impacus.maketplace.service.product.ShoppingBasketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<Boolean> addShoppingBasket(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateShoppingBasketDTO shoppingBasketRequest) {
        Boolean result = shoppingBasketService.addShoppingBasket(user.getId(), shoppingBasketRequest);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(result)
                .build();
    }

    /**
     * 장바구니 데이터들를 삭제하는 API
     *
     * @param shoppingBasketList
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteShoppingBasket(@RequestParam(name = "shopping-basket-id") List<Long> shoppingBasketList) {
        shoppingBasketService.deleteAllShoppingBasket(shoppingBasketList);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * 장바구니 수량을 수정하는 API
     *
     * @param shoppingBasketRequest
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("")
    public ApiResponseEntity<Boolean> updateShoppingBasket(
            @Valid @RequestBody ChangeShoppingBasketQuantityDTO shoppingBasketRequest) {
        Boolean result = shoppingBasketService.updateShoppingBasket(shoppingBasketRequest);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(result)
                .build();
    }

    /**
     * 장바구니 데이터 조회 API
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<Slice<ShoppingBasketDetailDTO>> getShoppingBasket(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        Slice<ShoppingBasketDetailDTO> dto = shoppingBasketService.getAllShoppingBasket(user.getId(), pageable);
        return ApiResponseEntity
                .<Slice<ShoppingBasketDetailDTO>>builder()
                .data(dto)
                .build();
    }
}
