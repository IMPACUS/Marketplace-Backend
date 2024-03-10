package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.common.enumType.OrderStatus;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.dto.order.request.CreateOrderRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class OrderSteps {
    public static ExtractableResponse<Response> 주문_추가_요청(String accessToken, CreateOrderRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("shoppingBasketId", request.getShoppingBasketId());
        params.put("paymentMethod", request.getPaymentMethod());
        params.put("orderStatus", request.getOrderStatus());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/v1/order")
                .then().log().all().extract();
    }

    public static CreateOrderRequest createCreateOrderRequest(Long shoppingBasketId, OrderStatus orderStatus, PaymentMethod paymentMethod) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setShoppingBasketId(shoppingBasketId);
        createOrderRequest.setOrderStatus(orderStatus);
        createOrderRequest.setPaymentMethod(paymentMethod);

        return createOrderRequest;
    }
}
