package com.impacus.maketplace.service.order;

import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.OrderUtils;
import com.impacus.maketplace.dto.order.response.CheckoutProductDTO;
import com.impacus.maketplace.dto.order.response.OrderCheckoutCartDTO;
import com.impacus.maketplace.dto.order.response.OrderCheckoutProductDTO;
import com.impacus.maketplace.redis.service.OrderNumberService;
import com.impacus.maketplace.repository.order.OrderRepositroy;
import com.impacus.maketplace.repository.order.querydsl.OrderCustomRepository;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsByCartDTO;
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

    private final OrderCustomRepository orderCustomRepository;
    private final OrderRepositroy orderRepositroy;
    private final OrderNumberService orderNumberService;

    /**
     * 단일 주문 상품 조회
     * @param productId       주문 상품 id
     * @param productOptionId 주문 상품 option id
     * @param quantity        주문 수량
     */
    @Transactional
    public OrderCheckoutProductDTO getCheckoutSingle(Long productId, Long productOptionId, Long quantity) {

        // 1. 필요한 데이터 전부 가져오기
        OrderProductWithDetailsDTO orderProductWithDeatilsDTO = orderCustomRepository.findOrderProductWithDetails(productId, productOptionId);

        // 2. 유효성 검증
        if (orderProductWithDeatilsDTO == null) {
            throw new CustomException(OrderErrorType.NOT_FOUND_ORDER_PRODUCT);
        }
        validateCheckoutProduct(orderProductWithDeatilsDTO.isProductIsDeleted(), orderProductWithDeatilsDTO.isOptionIsDeleted(), orderProductWithDeatilsDTO.getProductStatus(), orderProductWithDeatilsDTO.getStock(), quantity);

        // 3. 주문 번호 생성
        String orderNumber = generateOrderNumber();

        // 3. 필요 데이터 DTO로 변환 후 내려주기
        CheckoutProductDTO product = new CheckoutProductDTO(orderProductWithDeatilsDTO, productId, productOptionId, quantity);
        return new OrderCheckoutProductDTO(orderNumber, product);
    }

    /**
     * 장바구나 id List를 이용해서 주문 상품 조회
     * @param shoppingBasketIdList 장바구니 id List
     */
    public OrderCheckoutCartDTO getCheckoutCart(List<Long> shoppingBasketIdList) {

        // 1. 필요한 모든 데이터 가져오기
        List<OrderProductWithDetailsByCartDTO> orderProductWithDetailsByCartDTOList = orderCustomRepository.findOrderProductWithDetailsByCart(shoppingBasketIdList);

        // 2. 유효성 검증
        if (orderProductWithDetailsByCartDTOList.isEmpty()) {
            throw new CustomException(OrderErrorType.NOT_FOUND_ORDER_PRODUCT);
        }
        orderProductWithDetailsByCartDTOList.forEach(orderProductWithDetailsByCartDTO ->
                validateCheckoutProduct(orderProductWithDetailsByCartDTO.isProductIsDeleted(),
                        orderProductWithDetailsByCartDTO.isOptionIsDeleted(),
                        orderProductWithDetailsByCartDTO.getProductStatus(),
                        orderProductWithDetailsByCartDTO.getStock(),
                        orderProductWithDetailsByCartDTO.getQuantity()));

        // 3. 주문 번호 생성
        String orderNumber = generateOrderNumber();

        List<CheckoutProductDTO> products = orderProductWithDetailsByCartDTOList.stream()
                .map(CheckoutProductDTO::new)
                .toList();
        return new OrderCheckoutCartDTO(orderNumber, products);
    }
    private String generateOrderNumber() {
        String orderNumber = orderNumberService.generateAndSaveOrderNumber();
        while (orderRepositroy.existsByOrderNumber(orderNumber)) {
            orderNumber = orderNumberService.generateAndSaveOrderNumber();
        }

        return orderNumber;
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
}
