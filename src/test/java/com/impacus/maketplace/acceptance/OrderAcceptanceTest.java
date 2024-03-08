package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.impacus.maketplace.acceptance.ProductSteps.shoppingBasketRequest;
import static com.impacus.maketplace.acceptance.ProductSteps.장바구니_추가_요청;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
	private Long 상품옵션 = 1L;
	@BeforeEach
	public void setUp() {
		super.setUp();

		ShoppingBasketRequest shoppingBasketRequest = shoppingBasketRequest(상품옵션, 80L);

		ExtractableResponse<Response> response = 장바구니_추가_요청(관리자, shoppingBasketRequest);
	}

	@DisplayName("주문 생성")
	@Test
	void createOrder() {

	}
}
