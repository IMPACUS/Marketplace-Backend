package com.impacus.maketplace.service.product;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.repository.product.ProductRepository;
import com.impacus.maketplace.repository.product.WishlistRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeleteProductService {
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final AttachFileService attachFileService;
    private final ProductDescriptionService productDescriptionService;
    private final WishlistRepository wishlistRepository;
    private final ReadProductService readProductService;


    /**
     * 판매자 다중 삭제 함수
     * - 판매자인 경우, 판매자가 등록한 상품만 삭제 가능
     * - 관리자인 경우, 모든 상품 삭제 가능
     *
     * @param productIdList
     */
    @Transactional
    public void deleteAllProduct(Long userId, List<Long> productIdList) {
        try {
            // 1. 권한 확인
            UserType role = SecurityUtils.getCurrentUserType();

            // 1-1. 판매자인 경우, 판매자 등록 상품인지 확인
            if (role == UserType.ROLE_APPROVED_SELLER && !readProductService.verifySellerProductIds(userId, productIdList)) {
                throw new CustomException(ProductErrorType.PRODUCT_ACCESS_DENIED);
            }

            // 2. 상품 삭제
            productIdList
                    .forEach(this::deleteProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * Product 삭제하는 함수 (isDelete가 true로 변경)
     * 1. ProductOption 삭제
     * 2. Product 대표 이미지 삭제
     * 3. Product 삭제
     *
     * @param productId
     */
    @Transactional
    public void deleteProduct(Long productId) {
        try {
            // 1. Product 존재 확인
            Product deleteProduct = readProductService.findProductById(productId);

            // 2. ProductOption 삭제
            productOptionService.deleteAllProductionOptionByProductId(deleteProduct.getId());

            // 3. Product 대표 이미지 삭제
            attachFileService.deleteAttachFileByReferencedId(deleteProduct.getId(), ReferencedEntityType.PRODUCT);

            // 4. 찜 데이터 삭제
            wishlistRepository.deleteByProductId(productId);

            // 5. 삭제
            productRepository.deleteById(productId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
