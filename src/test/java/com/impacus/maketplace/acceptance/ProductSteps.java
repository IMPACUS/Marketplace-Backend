package com.impacus.maketplace.acceptance;

import com.impacus.maketplace.dto.product.request.ProductDetailInfoRequest;
import com.impacus.maketplace.dto.product.request.ProductOptionRequest;
import com.impacus.maketplace.dto.shoppingBasket.request.ShoppingBasketRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSteps {
	public static ExtractableResponse<Response> 상품_등록_요청(String accessToken, Long brandId, String name, String description, String deliveryType, String categoryType, Integer deliveryFee, Integer refundFee, Integer marketPrice, Integer appSalesPrice, Integer discountPrice, Integer weight, String productStatus, ProductDetailInfoRequest productDetailInfoRequest, List<ProductOptionRequest> productOptionRequests) {

		Map<String, Object> params = new HashMap<>();

		params.put("brandId", brandId);
		params.put("name", name);
		params.put("description", description);
		params.put("deliveryType", deliveryType);
		params.put("categoryType", categoryType);
		params.put("deliveryFee", deliveryFee);
		params.put("refundFee", refundFee);
		params.put("marketPrice", marketPrice);
		params.put("appSalesPrice", appSalesPrice);
		params.put("discountPrice", discountPrice);
		params.put("weight", weight);
		params.put("productStatus", productStatus);
		params.put("productDetailInfoRequest", productDetailInfoRequest);
		params.put("productOptionRequests", productOptionRequests);

		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/api/v1/product/seller")
				.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 장바구니_추가_요청(String accessToken, ShoppingBasketRequest shoppingBasketRequest) {
		Map<String, Object> params = new HashMap<>();
		params.put("shoppingBasketRequest", shoppingBasketRequest);

		return RestAssured
				.given().log().all()
				.auth().oauth2(accessToken)
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/api/v1/product/seller")
				.then().log().all().extract();
	}

	public static ProductDetailInfoRequest createProductDetailInfoRequest(String productType, String productMaterial, String productColor, String productSize, String dateOfManufacture, String washingPrecautions, String countryOfManufacture, String manufacturer, String importer, String qualityAssuranceStandards, String asManager, String contactNumber) {
		ProductDetailInfoRequest productDetailInfoRequest = new ProductDetailInfoRequest();
		productDetailInfoRequest.setProductType(productType);
		productDetailInfoRequest.setProductMaterial(productMaterial);
		productDetailInfoRequest.setProductColor(productColor);
		productDetailInfoRequest.setProductSize(productSize);
		productDetailInfoRequest.setDateOfManufacture(dateOfManufacture);
		productDetailInfoRequest.setWashingPrecautions(washingPrecautions);
		productDetailInfoRequest.setCountryOfManufacture(countryOfManufacture);
		productDetailInfoRequest.setManufacturer(manufacturer);
		productDetailInfoRequest.setImporter(importer);
		productDetailInfoRequest.setQualityAssuranceStandards(qualityAssuranceStandards);
		productDetailInfoRequest.setAsManager(asManager);
		productDetailInfoRequest.setContactNumber(contactNumber);

		return productDetailInfoRequest;
	}

	public static ShoppingBasketRequest shoppingBasketRequest(Long productOptionId, Long quantity) {
		ShoppingBasketRequest shoppingBasketRequest = new ShoppingBasketRequest();
		shoppingBasketRequest.setProductOptionId(productOptionId);
		shoppingBasketRequest.setQuantity(quantity);

		return shoppingBasketRequest;
	}
}
