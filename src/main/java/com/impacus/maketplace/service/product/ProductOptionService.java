package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.product.request.CreateProductOptionDTO;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
import com.impacus.maketplace.repository.product.ShoppingBasketRepository;
import com.impacus.maketplace.service.product.history.ProductOptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ShoppingBasketRepository shoppingBasketRepository;
    private final ProductOptionHistoryService productOptionHistoryService;

    /**
     * ProductOptionRequest 리스트를 ProductOption 객체로 모두 저장하는 함수
     *
     * @param productId
     * @param productOptionRequestList
     * @return
     */
    @Transactional
    public List<ProductOption> addProductOption(Long productId, List<CreateProductOptionDTO> productOptionRequestList) {
        List<ProductOption> newProductOptions = productOptionRequestList.stream()
                .map(productOptionRequest -> productOptionRequest.toEntity(productId))
                .collect(Collectors.toList());

        saveAllProductOptions(newProductOptions);

        return newProductOptions;
    }

    /**
     * 상품 옵션 리스트 일괄 저장
     *
     * @param productOptions
     */
    @Transactional
    public void saveAllProductOptions(List<ProductOption> productOptions) {
        // 1. 상품 옵션 생성
        productOptionRepository.saveAll(productOptions);

        // 2. 상품 옵션 이력 생성
        productOptionHistoryService.saveAllProductOptionHistory(productOptions);
    }

    /**
     * 전달받은 productId로 ProductOption 을 찾는 함수
     *
     * @param productId
     * @return
     */
    public List<ProductOption> findProductOptionByProductId(Long productId) {
        return productOptionRepository.findByProductId(productId);
    }

    /**
     * productId와 연결된 모든 ProductOption 을 삭제하는 함수
     *
     * @param productId
     */
    @Transactional
    public void deleteAllProductionOptionByProductId(Long productId) {
        List<ProductOption> productOptions = findProductOptionByProductId(productId);

        deleteAllProductOption(productOptions);
    }

    /**
     * productOption 데이터와 연결된 Shopping cart 데이터와 상품 옵션을 삭제하는 함수
     *
     * @param productOptions
     */
    public void deleteAllProductOption(List<ProductOption> productOptions) {
        List<Long> productOptionIds = productOptions.stream().map(ProductOption::getId).toList();

        shoppingBasketRepository.deleteByProductOptionId(productOptionIds);
        productOptionRepository.updateIsDeleteTrueByIds(productOptionIds);
    }

    /**
     * production option을 수정하는 함수
     * - productOptionId 존재: 기존 데이터 수정
     * - productOptionId 존재 X: 생성
     * - productOptionRequestList 존재하지 않는 기존 데이터는 삭제
     *
     * @param productId
     * @param productOptionRequestList
     */
    @Transactional
    public void updateProductOptionList(Long productId, List<CreateProductOptionDTO> productOptionRequestList) {
        List<ProductOption> productOptionList = findProductOptionByProductId(productId);

        // 1. 전달 받은 데이터 중 생성&수정할 데이터 취합
        List<ProductOption> addedProductOptions = new ArrayList<>();
        List<ProductOption> updatedProductOptions = new ArrayList<>();
        for (CreateProductOptionDTO productOptionRequest : productOptionRequestList) {

            if (productOptionRequest.getProductOptionId() == null) {
                // 1-1 새로운 상품 옵션인 경우
                ProductOption productOption = productOptionRequest.toEntity(productId);
                addedProductOptions.add(productOption);
            } else {
                // 1-2 수정된 상품 옵션인 경우
                ProductOption modifiedData = productOptionList.stream()
                        .filter(p -> Objects.equals(p.getId(), productOptionRequest.getProductOptionId()))
                        .findAny()
                        .orElse(null);

                // null check 후, 수정
                if (modifiedData == null) {
                    throw new CustomException(ProductErrorType.NOT_EXISTED_PRODUCT_OPTION);
                } else {
                    modifiedData.setColor(productOptionRequest.getColor());
                    modifiedData.setSize(productOptionRequest.getSize());
                    modifiedData.setStock(productOptionRequest.getStock());

                    updatedProductOptions.add(modifiedData);
                    productOptionList.remove(modifiedData);
                }
            }
        }

        LogUtils.writeInfoLog(
                "ProductOptionService",
                String.format("product option updated: create {%s} update {%s} delete {%s}",
                        addedProductOptions.size(),
                        updatedProductOptions.size(),
                        productOptionList.size())
        );

        // 2. 생성
        saveAllProductOptions(addedProductOptions);

        // 3. 수정
        for (ProductOption productOption : updatedProductOptions) {
            addProductOptionHistoryInUpdateMode(productOption);
            productOptionRepository.updateProductOptionById(
                    productOption.getId(),
                    productOption.getColor(),
                    productOption.getSize(),
                    productOption.getStock()
            );
        }

        // 4. 전달 받지 않은 옵션 데이터 삭제
        deleteAllProductOption(productOptionList);
    }

    /**
     * 상품 옵션 이력 생성
     *
     * @param productOption
     */
    public void addProductOptionHistoryInUpdateMode(ProductOption productOption) {
        // 1. 색상 혹은 크기 값이 변경되었는지 확인
        if (!productOption.getColor().equals(productOption.getPreviousColor()) ||
                !productOption.getSize().equals(productOption.getPreviousSize())) {
            // 2. ProductOption 이벤트 생성
            productOptionHistoryService.saveAllProductOptionHistory(List.of(productOption));
        }
    }
}
