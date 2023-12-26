package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.product.request.WishlistRequest;
import com.impacus.maketplace.dto.product.response.WishlistDTO;
import com.impacus.maketplace.entity.product.Wishlist;
import com.impacus.maketplace.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    /**
     * Wishlist를 저장하는 함수
     *
     * @param wishlistRequest
     * @return
     */
    @Transactional
    public WishlistDTO addWishlist(Long userId, WishlistRequest wishlistRequest) {
        // 1. 요청 데이터 유효성 확인
        validateWishList(userId, wishlistRequest);

        // 2. Wishlist 저장
        Wishlist newWishlist = Wishlist.builder()
                .productId(wishlistRequest.getProductId())
                .build();
        wishlistRepository.save(newWishlist);

        // 3. WishlistDTO로 반환
        return new WishlistDTO(newWishlist.getId(), newWishlist.getProductId());
    }

    public boolean validateWishList(Long userId, WishlistRequest wishlistRequest) {
        Long productId = wishlistRequest.getProductId();

        // 1. productId가 존재하는지 확인
        productService.findProductById(productId);

        // 2. 요청한 유저의 Wishlist에 상품이 존재하는지 확인
        if (!findWishlistByProductIdAndUserId(productId, userId).isEmpty()) {
            throw new CustomException(ErrorType.REGISTERED_WISHLIST);
        }

        return true;
    }

    /**
     * 상품에 사용자가 등록한 찜 객체 반환
     *
     * @param productId
     * @param userId
     * @return
     */
    public List<Wishlist> findWishlistByProductIdAndUserId(Long productId, Long userId) {
        return wishlistRepository.findByProductIdAndRegisterId(productId, userId.toString());
    }
}
