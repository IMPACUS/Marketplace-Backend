package com.impacus.maketplace.dto.order.response;

import com.impacus.maketplace.common.enumType.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetOrdersResponse {
	private Long orderId;
	private String orderUserName;
	private OrderStatus orderStatus;
	private LocalDateTime orderDate;
	private String productName;
	private String productSize;
	private String productColor;
	//todo: 배송처리

	@QueryProjection
	public GetOrdersResponse(final Long orderId, final String orderUserName, final OrderStatus orderStatus, final LocalDateTime orderDate, final String productName, final String productSize, final String productColor) {
		this.orderId = orderId;
		this.orderUserName = orderUserName;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
		this.productName = productName;
		this.productSize = productSize;
		this.productColor = productColor;
	}
}
