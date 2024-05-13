package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.shoppingBasket.request.ChangeShoppingBasketQuantityDTO;
import com.impacus.maketplace.dto.shoppingBasket.request.CreateShoppingBasketDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.SimpleShoppingBasketDTO;
import com.impacus.maketplace.entity.product.ShoppingBasket;
import com.impacus.maketplace.repository.product.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public SimpleShoppingBasketDTO addShoppingBasket(Long userId, CreateShoppingBasketDTO shoppingBasketRequest) {
        try {
            Optional<ShoppingBasket> optional = shoppingBasketRepository.findByProductOptionIdAndUserId(
                    shoppingBasketRequest.getProductOptionId(),
                    userId
            );
            if (optional.isPresent()) {
                ShoppingBasket shoppingBasket = optional.get();
                shoppingBasket.setQuantity(shoppingBasket.getQuantity() + shoppingBasketRequest.getQuantity());
                shoppingBasketRepository.save(shoppingBasket);

                return SimpleShoppingBasketDTO.toDTO(shoppingBasket);

            } else {
                ShoppingBasket shoppingBasket = shoppingBasketRequest.toEntity(userId);
                shoppingBasketRepository.save(shoppingBasket);

                return SimpleShoppingBasketDTO.toDTO(shoppingBasket);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * id로 ShoppingBasket를 찾는 함수
     *
     * @param shoppingBasketId
     * @return
     */
    public ShoppingBasket findShoppingBasketById(Long shoppingBasketId) {
        return shoppingBasketRepository.findById(shoppingBasketId)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_SHOPPING_CART));
    }

    /**
     * 장바구니에 상품의 수량을 수정하는 함수
     *
     * @param dto
     * @return
     */
    @Transactional
    public SimpleShoppingBasketDTO updateShoppingBasket(
            ChangeShoppingBasketQuantityDTO dto) {
        try {
            Long shoppingBasketId = dto.getShoppingBasketId();

            ShoppingBasket shoppingBasket = findShoppingBasketById(shoppingBasketId);
            shoppingBasket.setQuantity(dto.getQuantity());
            shoppingBasketRepository.save(shoppingBasket);

            return SimpleShoppingBasketDTO.toDTO(shoppingBasket);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 요청한 장바구니 데이터들을 삭제하는 함수 (isDelete가 true로 변경)
     *
     * @param shoppingBasketList
     */
    @Transactional
    public void deleteAllShoppingBasket(List<Long> shoppingBasketList) {
        try {
            // 1. ShoppingBasket 존재 확인
            List<ShoppingBasket> shoppingBaskets = shoppingBasketList.stream()
                    .map(this::findShoppingBasketById)
                    .collect(Collectors.toList());

            // 2. 삭제
            shoppingBasketRepository.deleteAllInBatch(shoppingBaskets);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 장바구니 데이터 조회하는 함수
     *
     * @param userId
     * @param pageable
     * @return
     */
    public Slice<ShoppingBasketDetailDTO> getAllShoppingBasket(Long userId, Pageable pageable) {
        try {
            return shoppingBasketRepository.findAllShoppingBasketByUserId(userId, pageable);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
