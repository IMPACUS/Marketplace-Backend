package com.impacus.maketplace.service.order;

import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.order.response.CheckoutProductDTO;
import com.impacus.maketplace.repository.order.querydsl.OrderCustomRepository;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    public OrderCustomRepository orderCustomRepository;

    /**
     * 단일 주문 상품 조회
     * @param productId 주문 상품 id
     * @param productOptionId 주문 상품 option id
     * @param quantity 주문 수량
     */
    public CheckoutProductDTO getCheckoutSingle(Long productId, Long productOptionId, Long quantity) {

        // 1. 필요한 데이터 전부 가져오기
        OrderProductWithDetailsDTO orderProductWithDeatilsDTO = orderCustomRepository.findOrderProductWithDetails(productId, productOptionId);

        // 2. 유효성 검증
        validateCheckoutProduct(orderProductWithDeatilsDTO.isProductIsDeleted(), orderProductWithDeatilsDTO.isOptionIsDeleted(), orderProductWithDeatilsDTO.getProductStatus(), orderProductWithDeatilsDTO.getStock(), quantity);

        // 3. 필요 데이터 DTO로 변환 후 내려주기
        return new CheckoutProductDTO(orderProductWithDeatilsDTO, productId, productOptionId, quantity);
    }

    private void validateCheckoutProduct(boolean productIsDeleted, boolean productOptionIsDeleted, ProductStatus productStatus, Long stock, Long quantity) {

        // 1. 상품이 삭제 되었는지
        if (productIsDeleted) {
            throw new CustomException(OrderErrorType.DELETED_ORDER_PRODUCT);
        }

        // 2. 상품의 상태가 판매중인지
        if (!productStatus.equals(ProductStatus.SALES_PROGRESS)) {
            if (productStatus.equals(ProductStatus.SALES_STOP)) {
                throw new CustomException(OrderErrorType.SALE_STOP_ORDER_PRODUCT);
            }
            if (productStatus.equals(ProductStatus.SOLD_OUT)) {
                throw new CustomException(OrderErrorType.SOLD_OUT_ORDER_PRODUCT);
            }
        }

        // 3. 옵션이 삭제되었는지
        if (productOptionIsDeleted) {
            throw new CustomException(OrderErrorType.DELETED_ORDER_PRODUCT_OPTION);
        }

        // 4. 재고가 부족한지
        if (stock < quantity) {
            throw new CustomException(OrderErrorType.OUT_OF_STOCK_ORDER_PRODUCT);
        }
    }

    public void getCheckoutCart(Long userId, List<Long> productIdList) {

        // 1. 필요한 모든 데이터 가져오기
        orderCustomRepository.findOrderProductWithDetailsByCart(userId, productIdList);

    }
}
