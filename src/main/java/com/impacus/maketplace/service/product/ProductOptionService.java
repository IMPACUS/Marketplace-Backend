package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.product.request.ProductOptionRequest;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
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


    /**
     * ProductOptionRequest 리스트를 ProductOption 객체로 모두 저장하는 함수
     *
     * @param productId
     * @param productOptionRequestList
     * @return
     */
    @Transactional
    public void addProductOption(Long productId, List<ProductOptionRequest> productOptionRequestList) {
        List<ProductOption> newProductOptions = productOptionRequestList.stream()
                .map(productOptionRequest -> productOptionRequest.toEntity(productId))
                .collect(Collectors.toList());

        saveAllProductOptions(newProductOptions);
    }

    /**
     * 상품 옵션 리스트 일괄 저장
     *
     * @param productOptions
     */
    @Transactional
    public void saveAllProductOptions(List<ProductOption> productOptions) {
        productOptionRepository.saveAll(productOptions);
    }

    /**
     * ProductOption을 DB에 저장하는 함수
     *
     * @param newProductOption
     */
    @Transactional
    public ProductOption saveProductOption(ProductOption newProductOption) {
        return productOptionRepository.save(newProductOption);
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
     * productId와 연결된 모든 ProductOption을 삭제하는 함수
     *
     * @param productId
     */
    @Transactional
    public void deleteAllProductionOptionByProductId(Long productId) {
        List<ProductOption> productOptions = findProductOptionByProductId(productId);

        deleteAllProductOption(productOptions);
    }

    /**
     * productOption 데이터를 모두 삭제하는 함수
     *
     * @param productOptions
     */
    public void deleteAllProductOption(List<ProductOption> productOptions) {
        productOptionRepository.deleteAllInBatch(productOptions);
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
    public void updateProductOptionList(Long productId, List<ProductOptionRequest> productOptionRequestList) {
        List<ProductOption> productOptionList = findProductOptionByProductId(productId);

        // 1. 전달 받은 데이터 중 생성&수정할 데이터 취합
        List<ProductOption> updatedProductOptionList = new ArrayList<>();
        for (ProductOptionRequest productOptionRequest : productOptionRequestList) {
            if (productOptionRequest.getProductOptionId() == null) {
                ProductOption productOption = productOptionRequest.toEntity(productId);
                updatedProductOptionList.add(productOption);
            } else {
                ProductOption modifiedData = productOptionList.stream()
                        .filter(p -> Objects.equals(p.getId(), productOptionRequest.getProductOptionId()))
                        .findAny()
                        .orElse(null);

                if (modifiedData == null) {
                    // 생성
                    throw new CustomException(ErrorType.NOT_EXISTED_PRODUCT_OPTION);
                } else {
                    // 수정
                    modifiedData.setColor(productOptionRequest.getColor());
                    modifiedData.setSize(productOptionRequest.getSize());
                    modifiedData.setStock(productOptionRequest.getStock());

                    updatedProductOptionList.add(modifiedData);
                    productOptionList.remove(modifiedData);
                }
            }
        }

        // 2. 생성&수정
        productOptionRepository.saveAll(updatedProductOptionList);

        // 3. 전달 받지 않은 옵션 데이터 삭제
        deleteAllProductOption(productOptionList);
    }

    /**
     * productOption 삭제
     * - 연결되어 있는 장바구니 데이터 삭제
     *
     * @param productOption
     */
    public void deleteProductOption(ProductOption productOption) {

    }
}
