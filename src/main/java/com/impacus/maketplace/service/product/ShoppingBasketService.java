package com.impacus.maketplace.service.product;

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
     * 장바구니에 상품을 추가하는 API
     * - 사용자에서 이미 해당 상품 옵션이 추가되어 있는지 확인
     * - 존재: quantity 값 더하기
     * - 존재X: 새로 생성
     *
     * @param shoppingBasketRequest
     * @return
     */
    @Transactional
    public SimpleShoppingBasketDTO addShoppingBasket(Long userId, ShoppingBasketRequest shoppingBasketRequest) {
        Optional<ShoppingBasket> optional = shoppingBasketRepository.findByProductOptionIdAndRegisterId(
                shoppingBasketRequest.getProductOptionId(),
                userId.toString()
        );
        if (optional.isPresent()) {
            ShoppingBasket shoppingBasket = optional.get();
            shoppingBasket.setQuantity(shoppingBasketRequest.getQuantity());
            shoppingBasketRepository.save(shoppingBasket);

            return SimpleShoppingBasketDTO.toDTO(shoppingBasket);

        } else {
            ShoppingBasket shoppingBasket = shoppingBasketRequest.toEntity();
            shoppingBasketRepository.save(shoppingBasket);

            return SimpleShoppingBasketDTO.toDTO(shoppingBasket);
        }
    }
}
