package com.impacus.maketplace.acceptance;

import org.junit.jupiter.api.DisplayName;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
	private Long 상품옵션 = 1L;
	private Long 쇼핑바구니;

//	@BeforeEach
//	public void setUp() {
//		super.setUp();
//
//		ShoppingBasketDTO shoppingBasketRequest = shoppingBasketRequest(상품옵션, 80L);
//
//		ExtractableResponse<Response> response = 장바구니_추가_요청(관리자, shoppingBasketRequest);
//		쇼핑바구니 = response.jsonPath().getLong("data.id");
//	}
//
//	@DisplayName("주문 생성")
//	@Test
//	void createOrder() {
//		CreateOrderRequest createOrderRequest = createCreateOrderRequest(쇼핑바구니, OrderStatus.SOLD_OUT, PaymentMethod.CARD);
//		ExtractableResponse<Response> response = 주문_추가_요청(관리자, createOrderRequest);
//
//		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//	}
//
//	@DisplayName("주문리스트 조회")
//	@Test
//	void getOrders() {
//		CreateOrderRequest createOrderRequest = createCreateOrderRequest(쇼핑바구니, OrderStatus.SOLD_OUT, PaymentMethod.CARD);
//		주문_추가_요청(관리자, createOrderRequest);
//		주문_추가_요청(관리자, createOrderRequest);
//		주문_추가_요청(관리자, createOrderRequest);
//
//		String startAt = "2024-03-01";
//		String endAt = "2024-03-31";
//
//		ExtractableResponse<Response> response = 주문_전체_조회_요청(관리자, startAt, endAt);
//
//		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//		Assertions.assertThat(response.jsonPath().getList("data.content")).hasSize(3);
//	}
}
