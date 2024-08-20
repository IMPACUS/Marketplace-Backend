package com.impacus.maketplace.controller.product;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.wishlist.request.CreateWishlistDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import com.impacus.maketplace.service.product.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("")
    public ApiResponseEntity<WishlistDTO> addWishlist(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateWishlistDTO wishlistRequest) {
        WishlistDTO wishlistDTO = wishlistService.addWishlist(user.getId(), wishlistRequest);
        return ApiResponseEntity
                .<WishlistDTO>builder()
                .data(wishlistDTO)
                .build();
    }

    /**
     * 등록된 찜 데이터들를 삭제하는 API
     *
     * @param wishlistIdList
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteWishlist(@RequestParam(name = "wishlist-id") List<Long> wishlistIdList) {
        wishlistService.deleteAllWishlist(wishlistIdList);
        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * 찜 데이터 조회 API
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("")
    public ApiResponseEntity<Slice<WishlistDetailDTO>> getWishlist(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 15) Pageable pageable) {
        Slice<WishlistDetailDTO> wishlistDetailDTOS = wishlistService.getWishlists(user.getId(), pageable);
        return ApiResponseEntity
                .<Slice<WishlistDetailDTO>>builder()
                .data(wishlistDetailDTOS)
                .build();
    }
}
