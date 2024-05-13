package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.wishlist.request.CreateWishlistDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDTO;
import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import com.impacus.maketplace.entity.product.Wishlist;
import com.impacus.maketplace.repository.product.WishlistRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final AttachFileService attachFileService;

    /**
     * Wishlist를 저장하는 함수
     *
     * @param wishlistRequest
     * @return
     */
    @Transactional
    public WishlistDTO addWishlist(Long userId, CreateWishlistDTO wishlistRequest) {
        // 1. 요청 데이터 유효성 확인
        validateWishList(userId, wishlistRequest);

        // 2. Wishlist 저장
        Wishlist newWishlist = wishlistRequest.toEntity();
        wishlistRepository.save(newWishlist);

        // 3. WishlistDTO로 반환
        return WishlistDTO.toDTO(newWishlist);
    }

    public boolean validateWishList(Long userId, CreateWishlistDTO wishlistRequest) {
        Long productId = wishlistRequest.getProductId();

        // 1. productId가 존재하는지 확인
        productService.findProductByIdAndIsDeletedFalse(productId);

        // 2. 요청한 유저의 Wishlist에 상품이 존재하는지 확인
        if (!findWishlistByProductIdAndUserId(productId, userId).isEmpty()) {
            throw new CustomException(CommonErrorType.REGISTERED_WISHLIST);
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

    /**
     * id로 Wishlist를 찾는 함수
     *
     * @param wishlistId
     * @return
     */
    public Wishlist findWishlistById(Long wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new CustomException((CommonErrorType.NOT_EXISTED_WISHLIST)));
    }

    /**
     * Wishlist들을 삭제하는 함수 (isDelete가 true로 변경)
     *
     * @param wishlistIdList
     */
    @Transactional
    public void deleteAllWishlist(List<Long> wishlistIdList) {
        try {
            // 1. Wishlist 존재 확인
            List<Wishlist> wishlists = wishlistIdList.stream()
                    .map(this::findWishlistById)
                    .collect(Collectors.toList());

            // 2. 삭제
            wishlistRepository.deleteAllInBatch(wishlists);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 찜 데이터를 조회하는 함수
     *
     * @param userId
     * @param pageable
     * @return
     */
    public Slice<WishlistDetailDTO> getAllWishlist(Long userId, Pageable pageable) {
        try {
            return wishlistRepository.findAllWishListByUserId(userId, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
