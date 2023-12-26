package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.product.request.WishlistRequest;
import com.impacus.maketplace.dto.product.response.WishlistDTO;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.product.WishlistService;
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
@RequestMapping("api/v1/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;


    @PostMapping("/user/new")
    public ApiResponseEntity<Object> addWishlist(@AuthenticationPrincipal CustomUserDetails user, @RequestBody WishlistRequest wishlistRequest) {
        WishlistDTO wishlistDTO = wishlistService.addWishlist(user.getId(), wishlistRequest);
        return ApiResponseEntity
                .builder()
                .data(wishlistDTO)
                .build();
    }
}
