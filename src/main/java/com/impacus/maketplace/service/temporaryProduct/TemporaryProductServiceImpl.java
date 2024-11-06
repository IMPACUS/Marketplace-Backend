package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.common.enumType.error.BundleDeliveryGroupErrorType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.request.*;
import com.impacus.maketplace.dto.product.response.WebProductBasicDTO;
import com.impacus.maketplace.dto.product.response.WebProductOptionDetailDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.IsExistedTemporaryProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.TemporaryProductDTO;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import com.impacus.maketplace.repository.product.bundleDelivery.BundleDeliveryGroupRepository;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductRepository;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductServiceImpl implements TemporaryProductService {
    private final TemporaryProductRepository temporaryProductRepository;
    private final TemporaryProductOptionService temporaryProductOptionService;
    private final TemporaryProductDetailInfoService temporaryProductDetailInfoService;
    private final ObjectCopyHelper objectCopyHelper;
    private final SubCategoryService subCategoryService;
    private final TemporaryProductDeliveryTimeService deliveryTimeService;
    private final TemporaryProductClaimService temporaryProductClaimService;
    private final BundleDeliveryGroupRepository bundleDeliveryGroupRepository;
    private final ReadSellerService readSellerService;

    @Override
    public IsExistedTemporaryProductDTO checkIsExistedTemporaryProduct(Long userId) {
        try {
            boolean isExisted = temporaryProductRepository.existsByRegisterId(userId.toString());
            return new IsExistedTemporaryProductDTO(isExisted);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    @Override
    @Transactional
    public void addOrModifyTemporaryProductAtBasic(Long userId, BasicStepProductDTO dto) {
        try {
            String registerId = userId.toString();
            // 유효성 검사
            validateTemporaryProductAtBasic(userId, dto);

            // 임시 저장 상품이 존재하는지 확인
            if (temporaryProductRepository.existsByRegisterId(registerId)) {
                // 임시 저장 상품 수정
                updateTemporaryProductAtBasic(registerId, dto);
            } else {
                // 임시 저장 상품 저장
                addTemporaryProductAtBasic(dto);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    private void validateTemporaryProductAtBasic(Long userId, BasicStepProductDTO dto) {
        UserType userType = SecurityUtils.getCurrentUserType();

        // 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (dto.getProductImages().size() > 5) {
            throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        // 카테고리 존재 확인
        if (!subCategoryService.existsBySubCategoryId(dto.getCategoryId())) {
            throw new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY);
        }

        // 판매자 존재하는지 확인
        if (!userType.equals(UserType.ROLE_APPROVED_SELLER) && dto.getSellerId() == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "sellerId는 null일 수 없습니다.");
        }

        // 판매자에 등록된 묶음 배송인지 확인
        Long sellerId = userType.equals(UserType.ROLE_APPROVED_SELLER) ? readSellerService.findSellerIdByUserId(userId) : dto.getSellerId();
        if (dto.getBundleDeliveryOption()  == BundleDeliveryOption.BUNDLE_DELIVERY_AVAILABLE
                && dto.getBundleDeliveryGroupId() != null
                && !bundleDeliveryGroupRepository.existsBySellerIdAndIdAndIsDeletedFalseAndIsUsedTrue(sellerId, dto.getBundleDeliveryGroupId())
        ) {
            throw new CustomException(BundleDeliveryGroupErrorType.NOT_EXISTED_BUNDLE_DELIVERY_GROUP);
        }
    }

    @Transactional
    public void addTemporaryProductAtBasic(BasicStepProductDTO dto) {
        UserType userType = SecurityUtils.getCurrentUserType();

        // 상품 생성
        TemporaryProduct temporaryProduct = dto.toEntity(userType);
        temporaryProductRepository.save(temporaryProduct);
        Long temporaryProductId = temporaryProduct.getId();

        // 배송 지연 시간 생성
        deliveryTimeService.addTemporaryProductDeliveryTime(temporaryProductId, dto.getDeliveryTime());

        // 상품 관련 연관 테이블 생성 (상품 상세 ,상품 클레임 정보, 배송 지연 시간)
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, new CreateProductDetailInfoDTO());
        temporaryProductClaimService.addTemporaryProductClaim(temporaryProductId, new CreateClaimInfoDTO());
    }

    @Transactional
    public void updateTemporaryProductAtBasic(String registerId, BasicStepProductDTO dto) {
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);
        UserType userType = SecurityUtils.getCurrentUserType();

        // 상품 수정
        temporaryProductRepository.updateTemporaryProduct(
                temporaryProductId,
                dto,
                !userType.equals(UserType.ROLE_APPROVED_SELLER)
        );

        // 배송 지연 시간 수정
        deliveryTimeService.updateTemporaryProductDeliveryTime(temporaryProductId, dto.getDeliveryTime());
    }

    @Override
    @Transactional
    public void addOrModifyTemporaryProductAtOptions(Long userId, OptionStepProductDTO dto) {
        try {
            // 1. 임시 저장 상품이 존재하는지 확인
            String registerId = userId.toString();
            if (temporaryProductRepository.existsByRegisterId(registerId)) {
                // 임시 저장 상품 수정
                updateTemporaryProductAtOptions(registerId, dto);
            } else {
                // 임시 저장 상품 저장
                addTemporaryProductAtOptions(dto);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    @Transactional
    public void addTemporaryProductAtOptions(OptionStepProductDTO dto) {
        // 상품 생성
        TemporaryProduct temporaryProduct = dto.toEntity();
        temporaryProductRepository.save(temporaryProduct);
        Long temporaryProductId = temporaryProduct.getId();

        // 상품 옵션 생성
        temporaryProductOptionService.addTemporaryProductOption(temporaryProductId, dto.getProductOptions());

        // 상품 관련 연관 테이블 생성 (상품 상세 ,상품 클레임 정보, 배송 지연 시간)
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, new CreateProductDetailInfoDTO());
        temporaryProductClaimService.addTemporaryProductClaim(temporaryProductId, new CreateClaimInfoDTO());
        deliveryTimeService.addTemporaryProductDeliveryTime(temporaryProductId, new CreateProductDeliveryTimeDTO());
    }

    @Transactional
    public void updateTemporaryProductAtOptions(String registerId, OptionStepProductDTO dto) {
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);

        // 상품 수정
        temporaryProductRepository.updateTemporaryProductAtOptions(temporaryProductId, dto);

        // 상품 옵션 수정
        temporaryProductOptionService.initializeTemporaryProductionOption(temporaryProductId, dto.getProductOptions());
    }

    @Override
    @Transactional
    public void addOrModifyTemporaryProductAtDetails(Long userId, DetailStepProductDTO dto) {
        try {
            // 1. 임시 저장 상품이 존재하는지 확인
            String registerId = userId.toString();
            if (temporaryProductRepository.existsByRegisterId(registerId)) {
                // 임시 저장 상품 수정
                updateTemporaryProductAtDetails(registerId, dto);
            } else {
                // 임시 저장 상품 저장
                addTemporaryProductAtDetails(dto);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    @Transactional
    public void addTemporaryProductAtDetails(DetailStepProductDTO dto) {
        TemporaryProduct newTemporaryProduct = dto.toEntity();
        temporaryProductRepository.save(newTemporaryProduct);
        Long temporaryProductId = newTemporaryProduct.getId();

        // 상품 상세 정보 저장
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, dto.getProductDetail());

        // 상품 클레임 정보 저장
        temporaryProductClaimService.addTemporaryProductClaim(temporaryProductId, dto.getClaim());

        // 상품 관련 연관 테이블 생성 (배송 지연 시간)
        deliveryTimeService.addTemporaryProductDeliveryTime(temporaryProductId, new CreateProductDeliveryTimeDTO());
    }

    @Transactional
    public void updateTemporaryProductAtDetails(String registerId, DetailStepProductDTO dto) {
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);

        // 상품 상세 수정
        temporaryProductRepository.updateTemporaryProductDetail(temporaryProductId, dto.getProductDetail());

        // 상품 클레임 정보 수정
        temporaryProductClaimService.updateTemporaryProductClaim(temporaryProductId, dto.getClaim());
    }

    @Override
    public TemporaryProduct findTemporaryProductByUserId(Long userId) {
        return temporaryProductRepository.findByRegisterId(userId.toString())
                .orElseThrow(() -> new CustomException(ProductErrorType.NOT_EXISTED_TEMPORARY_PRODUCT));
    }

    @Override
    @Transactional
    public void deleteTemporaryProduct(Long userId) {
        // 1. TemporaryProduct 존재 확인
        TemporaryProduct deleteTemporaryProduct = findTemporaryProductByUserId(userId);
        Long temporaryProductId = deleteTemporaryProduct.getId();

        // 2. TemporaryProductOption 삭제
        temporaryProductOptionService.deleteAllTemporaryProductionOptionByTemporaryProductId(temporaryProductId);

        // 배송지연시간, 상품 클레임 정보, 상품 상세 정보 삭제
        temporaryProductRepository.deleteRelationEntityById(temporaryProductId);

        // 4. 삭제
        temporaryProductRepository.deleteById(temporaryProductId);
    }

    @Override
    public TemporaryProductDTO findTemporaryProduct(Long userId, Long sellerId) {
        try {
            TemporaryProductDTO dto = getTemporaryProductDTO(userId);

            // 유효성 확인
            validateCategoryAndBundleDeliveryGroup(dto, userId, sellerId);

            // TemporaryProductOption 값 가져오기
            Set<WebProductOptionDetailDTO> options = getTemporaryProductOptions(dto.getId());
            dto.setProductOptions(options);
            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * dto 데이터 유효성 확인
     *
     * @param dto
     * @param sellerId
     */
    private void validateCategoryAndBundleDeliveryGroup(TemporaryProductDTO dto, Long userId, Long sellerId) {
        UserType userType = SecurityUtils.getCurrentUserType();
        WebProductBasicDTO basicDTO = dto.getInformation();

        // 카테고리 존재 확인
        if (!subCategoryService.existsBySubCategoryId(basicDTO.getCategoryId())) {
            dto.updateCategoryNull();
        }

        // 판매자 존재하는지 확인
        if (!userType.equals(UserType.ROLE_APPROVED_SELLER) && sellerId == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "sellerId는 null일 수 없습니다.");
        }

        // 판매자에 등록된 묶음 배송인지 확인
        Long foundSellerId = userType.equals(UserType.ROLE_APPROVED_SELLER) ? readSellerService.findSellerIdByUserId(userId) : sellerId;
        if (!bundleDeliveryGroupRepository.existsBySellerIdAndIdAndIsDeletedFalseAndIsUsedTrue(foundSellerId, basicDTO.getBundleDeliveryGroupId())) {
            dto.updateBundleDeliveryGroupNull();
        }
    }

    /**
     * 사용자 ID로 임시 상품을 조회 (상품이 없으면 예외를 발생)
     *
     * @param userId
     * @return
     */
    private TemporaryProductDTO getTemporaryProductDTO(Long userId) {
        TemporaryProductDTO dto = temporaryProductRepository.findDetailIdByRegisterId(userId.toString());
        if (dto == null) {
            throw new CustomException(ProductErrorType.NOT_EXISTED_TEMPORARY_PRODUCT);
        }
        return dto;
    }

    /**
     * 상품 ID로 임시 상품 옵션을 조회
     */
    private Set<WebProductOptionDetailDTO> getTemporaryProductOptions(Long productId) {
        return temporaryProductOptionService.findTemporaryProductOptionByProductId(productId)
                .stream()
                .map(option -> objectCopyHelper.copyObject(option, WebProductOptionDetailDTO.class))
                .collect(Collectors.toSet());
    }
}
