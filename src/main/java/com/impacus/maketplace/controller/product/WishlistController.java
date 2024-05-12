package com.impacus.maketplace.controller.product;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.wishlist.request.WishlistRequest;
import com.impacus.maketplace.dto.wishlist.response.WishlistDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import com.impacus.maketplace.service.product.WishlistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import security.CustomUserDetails;

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
    @PostMapping("/user")
    public ApiResponseEntity<Object> addWishlist(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody WishlistRequest wishlistRequest) {
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
    public ApiResponseEntity<Object> deleteWishlist(@RequestParam(name = "wishlist-id") List<Long> wishlistIdList) {
        wishlistService.deleteAllWishlist(wishlistIdList);
        return ApiResponseEntity
                .builder()
                .build();
    }

    /**
     * 찜 데이터 조회 API
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    public ApiResponseEntity<Object> getWishlist(@AuthenticationPrincipal CustomUserDetails user) {
        List<WishlistDetailDTO> wishlistDetailDTOS = wishlistService.getAllWishlist(user.getId());
        return ApiResponseEntity
                .builder()
                .data(wishlistDetailDTOS)
                .build();
    }
}
