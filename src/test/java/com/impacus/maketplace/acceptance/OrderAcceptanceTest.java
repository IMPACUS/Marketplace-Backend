package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.common.enumType.OrderStatus;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.dto.order.request.CreateOrderRequest;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.impacus.maketplace.acceptance.OrderSteps.createCreateOrderRequest;
import static com.impacus.maketplace.acceptance.OrderSteps.주문_추가_요청;
import static com.impacus.maketplace.acceptance.ProductSteps.shoppingBasketRequest;
import static com.impacus.maketplace.acceptance.ProductSteps.장바구니_추가_요청;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
	private Long 상품옵션 = 1L;
	private Long 쇼핑바구니;

	@BeforeEach
	public void setUp() {
		super.setUp();

		ShoppingBasketRequest shoppingBasketRequest = shoppingBasketRequest(상품옵션, 80L);

		ExtractableResponse<Response> response = 장바구니_추가_요청(관리자, shoppingBasketRequest);
		쇼핑바구니 = response.jsonPath().getLong("data.id");
	}

	@DisplayName("주문 생성")
	@Test
	void createOrder() {
		CreateOrderRequest createOrderRequest = createCreateOrderRequest(쇼핑바구니, OrderStatus.SOLD_OUT, PaymentMethod.CARD);
		ExtractableResponse<Response> response = 주문_추가_요청(관리자, createOrderRequest);

		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
