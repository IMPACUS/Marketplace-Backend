package com.impacus.maketplace.repository.order.querydsl;

import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderCustomRepository {

    OrderProductWithDetailsDTO findOrderProductWithDetails(Long productId, Long productOptionId);
    List<OrderProductWithDetailsByCartDTO> findOrderProductWithDetailsByCart(List<Long> shoppingBasketIdList);
}
