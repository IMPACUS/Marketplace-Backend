package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.dto.order.response.GetOrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderCustomRepository {
	Page<GetOrdersResponse> findAllOrderByUserId(Long userId, LocalDate startAt, LocalDate endAt, Pageable pageable);
}
