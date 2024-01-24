package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketForQuantityRequest;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import com.impacus.maketplace.dto.shoppingBasket.response.SimpleShoppingBasketDTO;
import com.impacus.maketplace.entity.product.ShoppingBasket;
import com.impacus.maketplace.repository.product.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingBasketService {
    private final ShoppingBasketRepository shoppingBasketRepository;

    /**
     * 장바구니에 상품을 추가하는 함수
     * - 사용자에서 이미 해당 상품 옵션이 추가되어 있는지 확인
     * - 존재: quantity 값 더하기
     * - 존재X: 새로 생성
     *
     * @param shoppingBasketRequest
     * @return
     */
    @Transactional
    public SimpleShoppingBasketDTO addShoppingBasket(Long userId, ShoppingBasketRequest shoppingBasketRequest) {
        try {
            Optional<ShoppingBasket> optional = shoppingBasketRepository.findByProductOptionIdAndRegisterId(
                    shoppingBasketRequest.getProductOptionId(),
                    userId.toString()
            );
            if (optional.isPresent()) {
                ShoppingBasket shoppingBasket = optional.get();
                shoppingBasket.setQuantity(shoppingBasket.getQuantity() + shoppingBasketRequest.getQuantity());
                shoppingBasketRepository.save(shoppingBasket);

                return SimpleShoppingBasketDTO.toDTO(shoppingBasket);

            } else {
                ShoppingBasket shoppingBasket = shoppingBasketRequest.toEntity();
                shoppingBasketRepository.save(shoppingBasket);

                return SimpleShoppingBasketDTO.toDTO(shoppingBasket);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    public ShoppingBasket findShoppingBasketById(Long shoppingBasketId) {
        return shoppingBasketRepository.findById(shoppingBasketId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_SHOPPING_CART));
    }

    /**
     * 장바구니에 상품의 수량을 수정하는 함수
     *
     * @param userId
     * @param shoppingBasketId
     * @param shoppingBasketRequest
     * @return
     */
    @Transactional
    public SimpleShoppingBasketDTO updateShoppingBasket(
            Long shoppingBasketId,
            ShoppingBasketForQuantityRequest shoppingBasketRequest) {
        try {
            ShoppingBasket shoppingBasket = findShoppingBasketById(shoppingBasketId);
            shoppingBasket.setQuantity(shoppingBasketRequest.getQuantity());
            shoppingBasketRepository.save(shoppingBasket);

            return SimpleShoppingBasketDTO.toDTO(shoppingBasket);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
