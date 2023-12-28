package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.WishlistRequest;
import com.impacus.maketplace.dto.product.response.WishlistDTO;
import com.impacus.maketplace.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;


    /**
     * 새로운 찜 데이터를 추가하는 API
     *
     * @param user
     * @param wishlistRequest
     * @return
     */
    @PostMapping("/user/new")
    public ApiResponseEntity<Object> addWishlist(@AuthenticationPrincipal CustomUserDetails user, @RequestBody WishlistRequest wishlistRequest) {
        WishlistDTO wishlistDTO = wishlistService.addWishlist(user.getId(), wishlistRequest);
        return ApiResponseEntity
                .builder()
                .data(wishlistDTO)
                .build();
    }

    /**
     * 등록된 찜 데이터들를 삭제하는 API
     *
     * @param wishlistIdList
     * @return
     */
    @DeleteMapping("/user")
    public ApiResponseEntity<Object> deleteWishlist(@RequestParam(name = "wishlistId") List<Long> wishlistIdList) {
        wishlistService.deleteWishlist(wishlistIdList);
        return ApiResponseEntity
                .builder()
                .build();
    }
}
