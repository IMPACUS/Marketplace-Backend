package com.impacus.maketplace.controller.order;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.order.request.CreateOrderRequest;
import com.impacus.maketplace.dto.order.response.GetOrdersResponse;
import com.impacus.maketplace.dto.order.response.OrderDTO;
import com.impacus.maketplace.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
	private final OrderService orderService;

	@PostMapping("")
	public ApiResponseEntity<?> createOrder(
			@AuthenticationPrincipal CustomUserDetails user,
			@Valid @RequestBody CreateOrderRequest createOrderRequest) {
		OrderDTO orderDTO = orderService.createOrder(user.getId(), createOrderRequest);
		return ApiResponseEntity
				.builder()
				.data(orderDTO)
				.build();
	}

	@GetMapping("/all")
	public ApiResponseEntity<?> getOrders(
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestParam(name = "startAt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
			@RequestParam(name = "endAt") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
			@PageableDefault(size = 12, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<GetOrdersResponse> getOrderResponses = orderService.getOrders(user.getId(), startAt, endAt, pageable);
		return ApiResponseEntity
				.builder()
				.data(getOrderResponses)
				.build();
	}
}
