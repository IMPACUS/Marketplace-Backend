package com.impacus.maketplace.service.api;

import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.product.response.*;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

/**
 * 상품 관련 인터페이스
 *
 * @author 김용현
 */
public interface ProductInterface {

    /**
     * 상품 ID에 대해 상품이 존재하는지 확인
     *
     * @param productId 상품 ID
     * @throws CustomException 존재하지 않으면 예외 발생하게 구현
     */
    void checkExistenceById(long productId);

    /**
     * ProductRequest 의 유효성 검사
     *
     * @param productImages 상품 이미지 리스트
     * @param categoryId       카테고리 ID
     * @throws CustomException 유효하지 않는 경우, 예외 발생
     */
    void validateProductRequest(
            List<String> productImages,
            Long categoryId,
            Long sellerId,
            BundleDeliveryOption bundleDeliveryOption,
            Long bundleDeliveryGroupId
    );

    /**
     * productId로 삭제된 상태가 아닌 Product 조회
     *
     * @param productId 상품 ID
     * @return 상품
     * @throws CustomException 존재하지 않은 경우, 예외 발생
     */
    Product findProductById(Long productId);

    /**
     * productIdList에 존재하는 모든 상품들이 userId인 판매자의 상품인지 확인
     * - true: 모두 판매자의 상품인 경우
     * - false: 판매자가 등록하지 않은 상품이 존재하는 경우
     *
     * @param userId        사용자 ID
     * @param productIdList 상품 ID 리스트
     * @return 모두 판매자의 상품인가에 대한 여부
     */
    boolean verifySellerProductIds(Long userId, List<Long> productIdList);

    /**
     * [앱] 전체 상품 조회
     *
     * @param userId        사용자 ID
     * @param subCategoryId 카테고리 ID
     * @param pageable      페이지네이션 정보
     * @return 상품 리스트
     */
    Slice<ProductForAppDTO> findProductByCategoryForApp(
            Long userId,
            Long subCategoryId,
            Pageable pageable
    );

    /**
     * 최근 본 상품 목록 조회
     *
     * @param userId   사용자 ID
     * @param pageable 페이지네이션 정보
     * @return 상품 리스트
     */
    Slice<ProductForAppDTO> findProductForRecentViews(
            Long userId,
            Pageable pageable
    );

    /**
     * [판매자/관리자] 상품 목록 페이지 조회
     * - 판매자인 경우, 판매자의 브랜드 등록 상품 조회
     * - 관리자인 경우, 등록되어 있는 모든 상품 조회
     *
     * @param userId   사용자 ID
     * @param keyword  검색어 (null/공백: 전체 반환, not null: keyword가 존재하는 데이터 반환)
     * @param startAt  조회할 기간의 시작 날짜 (해당 날짜 이후에 등록된 상품 조회)
     * @param endAt    조회할 기간의 종료 날짜 (해당 날짜 이전에 등록된 상품 조회)
     * @param pageable 페이지네이션 정보
     * @return 상품 리스트
     */
    Page<WebProductTableDetailDTO> findProductDetailsForWeb(
            Long userId,
            String keyword,
            LocalDate startAt,
            LocalDate endAt,
            Pageable pageable
    );

    /***
     * [앱] 상품 상세 정보 조회
     *
     * @param productId 상품 ID
     * @return 상품
     * @throws CustomException 존재하지 않은 경우, 예외 발생
     */
    AppProductDTO findDetailedProduct(Long userId, Long productId);

    /**
     * [판매자/관리자] 상품 전체 정보를 조회하는 함수
     *
     * @param userId    사용자 ID
     * @param productId 상품 ID
     * @return 상품
     * @throws CustomException 존재하지 않은 경우, 예외 발생
     */
    WebProductDetailDTO findProductDetailForWeb(Long userId, Long productId);

    /**
     * [판매자/관리자] 상품의 간략한 정보를 조회하는 함수
     * - 판매자: 판매자의 브랜드가 등록한 상품만 조회 가능
     * - 관리자: 모든 상품 조회 가능
     *
     * @param userId    사용자 ID
     * @param productId 상품 ID
     * @return
     */
    WebProductDTO findProductByProductId(Long userId, Long productId);

    /**
     * [관리자] 상품 등록 목록  조회
     *
     * @param keyword  검색어 (null/공백: 전체 반환, not null: keyword가 존재하는 데이터 반환)
     * @param startAt  조회할 기간의 시작 날짜 (해당 날짜 이후에 등록된 상품 조회)
     * @param endAt    조회할 기간의 종료 날짜 (해당 날짜 이전에 등록된 상품 조회)
     * @param pageable 페이지네이션 정보
     * @return 상품 리스트
     */
    Page<WebProductTableDTO> findProductsForWeb(
        Long sellerId,
        String keyword,
        LocalDate startAt,
        LocalDate endAt,
        Pageable pageable
    );
}
