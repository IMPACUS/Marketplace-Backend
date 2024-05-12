package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketDTO;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class ProductSteps {
	public static ExtractableResponse<Response> 장바구니_추가_요청(String accessToken, ShoppingBasketDTO shoppingBasketRequest) {
		Map<String, Object> params = new HashMap<>();
		params.put("productOptionId", shoppingBasketRequest.getProductOptionId());
		params.put("quantity", shoppingBasketRequest.getQuantity());

		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/api/v1/shopping-basket/user")
				.then().log().all().extract();
	}

	public static ShoppingBasketDTO shoppingBasketRequest(Long productOptionId, Long quantity) {
		ShoppingBasketDTO shoppingBasketRequest = new ShoppingBasketDTO();
		shoppingBasketRequest.setProductOptionId(productOptionId);
		shoppingBasketRequest.setQuantity(quantity);

		return shoppingBasketRequest;
	}
}
