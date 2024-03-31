package com.impacus.maketplace.service.order;

import com.impacus.maketplace.dto.order.request.CreateOrderRequest;
import com.impacus.maketplace.dto.order.response.GetOrdersResponse;
import com.impacus.maketplace.dto.order.response.OrderDTO;
import com.impacus.maketplace.entity.order.Order;
import com.impacus.maketplace.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderDTO createOrder(Long userId, CreateOrderRequest request) {
		Order order = CreateOrderRequest.toEntity(request, userId);
		orderRepository.save(order);
		return OrderDTO.toDTO(order);
	}

	public Page<GetOrdersResponse> getOrders(Long userId, LocalDate startAt, LocalDate endAt, Pageable pageable) {
		return orderRepository.findAllOrderByUserId(userId, startAt, endAt, pageable);
	}
}

