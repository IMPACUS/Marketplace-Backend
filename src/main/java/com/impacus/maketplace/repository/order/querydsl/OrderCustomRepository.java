package com.impacus.maketplace.repository.order.querydsl;

import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsDTO;

import java.util.List;

public interface OrderCustomRepository {

    OrderProductWithDetailsDTO findOrderProductWithDetails(Long productId, Long productOptionId);
    List<OrderProductWithDetailsByCartDTO> findOrderProductWithDetailsByCart(Long userId, List<Long> productIdList);
}
