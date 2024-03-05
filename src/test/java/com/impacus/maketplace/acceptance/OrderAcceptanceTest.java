package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.dto.product.request.ProductDetailInfoRequest;
import com.impacus.maketplace.dto.product.request.ProductOptionRequest;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.impacus.maketplace.acceptance.ProductSteps.*;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

	@BeforeEach
	public void setUp() {
		super.setUp();

		ProductDetailInfoRequest productDetailInfoRequest = createProductDetailInfoRequest("키친타올", "종이", "", "", "별도표기", "", "충남 아산", "키친산업", "", "", "", "");

		List<ProductOptionRequest> productOptionRequests = new ArrayList<>();
		ProductOptionRequest productOptionRequest1 = new ProductOptionRequest(1L, "", "S", 100L);
		ProductOptionRequest productOptionRequest2 = new ProductOptionRequest(2L, "", "M", 100L);
		ProductOptionRequest productOptionRequest3 = new ProductOptionRequest(3L, "", "L", 100L);
		productOptionRequests.add(productOptionRequest1);
		productOptionRequests.add(productOptionRequest2);
		productOptionRequests.add(productOptionRequest3);

		//todo: 상품 등록 요청시에 multipart 오류
		상품_등록_요청(관리자, 1L, "키친타올", "효과적으로 주방을 청소하는 키친타올", "GENERAL_DELIVERY", "TSHRT", 2500, 2500, 15000, 1500, 750, 230, "SALES_PROGRESS", productDetailInfoRequest, productOptionRequests);

		ShoppingBasketRequest shoppingBasketRequest = shoppingBasketRequest(productOptionRequest1.getProductOptionId(), 80L);

		ExtractableResponse<Response> response = 장바구니_추가_요청(관리자, shoppingBasketRequest);
	}

	@DisplayName("주문 생성")
	@Test
	void createOrder() {

	}
}
