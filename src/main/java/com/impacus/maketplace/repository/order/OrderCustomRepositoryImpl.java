package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.dto.order.response.GetOrdersResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.impacus.maketplace.entity.order.QOrder.order;
import static com.impacus.maketplace.entity.product.QProduct.product;
import static com.impacus.maketplace.entity.product.QProductOption.productOption;
import static com.impacus.maketplace.entity.product.QShoppingBasket.shoppingBasket;
import static com.impacus.maketplace.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GetOrdersResponse> findAllOrderByUserId(Long userId, LocalDate startAt, LocalDate endAt, Pageable pageable) {
		List<GetOrdersResponse> content = getOrdersContent(startAt, endAt);
		Long count = getOrdersCount(startAt, endAt);
		return new PageImpl<>(content, pageable, count);
	}

	private Long getOrdersCount(LocalDate startAt, LocalDate endAt) {
		return queryFactory.select(order.count())
				.from(order)
				.where(order.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
				.fetchOne();
	}

	private List<GetOrdersResponse> getOrdersContent(LocalDate startAt, LocalDate endAt) {

		return queryFactory.selectFrom(order)
				.leftJoin(shoppingBasket).on(order.shoppingBasketId.eq(shoppingBasket.id))
				.leftJoin(user).on(order.userId.eq(user.id))
				.leftJoin(productOption).on(shoppingBasket.productOptionId.eq(productOption.id))
				.leftJoin(product).on(productOption.productId.eq(product.id))
				.where(order.createAt.between(startAt.atStartOfDay(), endAt.atTime(LocalTime.MAX)))
				.select(
						Projections.constructor(GetOrdersResponse.class,
								order.id.as("orderId"),
								user.name.as("orderUserName"),
								order.orderStatus.as("orderStatus"),
								order.createAt.as("orderDate"),
								product.name.as("productName"),
								productOption.size.as("productSize"),
								productOption.color.as("productColor")
						)
				).fetch();
	}
}
