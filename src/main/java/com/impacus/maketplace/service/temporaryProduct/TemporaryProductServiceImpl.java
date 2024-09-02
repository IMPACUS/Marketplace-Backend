package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryErrorType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ProductErrorType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.common.utils.SecurityUtils;
import com.impacus.maketplace.dto.product.request.*;
import com.impacus.maketplace.dto.product.response.ProductClaimInfoDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.*;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductClaimInfo;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDeliveryTime;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDetailInfo;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.category.SubCategoryService;
import com.impacus.maketplace.service.seller.ReadSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductServiceImpl implements TemporaryProductService {
    private final TemporaryProductRepository temporaryProductRepository;
    private final AttachFileService attachFileService;
    private final TemporaryProductOptionService temporaryProductOptionService;
    private final TemporaryProductDetailInfoService temporaryProductDetailInfoService;
    private final ObjectCopyHelper objectCopyHelper;
    private final SubCategoryService subCategoryService;
    private final TemporaryProductDeliveryTimeService deliveryTimeService;
    private final TemporaryProductClaimService temporaryProductClaimService;

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
            // 1. 임시 저장 상품이 존재하는지 확인
            String registerId = userId.toString();
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

    @Transactional
    private void addTemporaryProductAtBasic(BasicStepProductDTO dto) {
        validateProductRequest(dto.getProductImages(), dto);

        // 상품 생성
        TemporaryProduct temporaryProduct = dto.toEntity();
        temporaryProductRepository.save(temporaryProduct);
        Long temporaryProductId = temporaryProduct.getId();

        // 배송 지연 시간 생성
        deliveryTimeService.addTemporaryProductDeliveryTime(temporaryProductId, dto.getDeliveryTime());

        // 상품 관련 연관 테이블 생성 (상품 상세 ,상품 클레임 정보, 배송 지연 시간)
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, new CreateProductDetailInfoDTO());
        temporaryProductClaimService.addTemporaryProductClaim(temporaryProductId, new CreateClaimInfoDTO());
    }

    @Transactional
    private void updateTemporaryProductAtBasic(String registerId, BasicStepProductDTO dto) {
        validateProductRequest(dto.getProductImages(), dto);
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);

        // 상품 수정
        temporaryProductRepository.updateTemporaryProduct(temporaryProductId, dto);

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
    private void addTemporaryProductAtOptions(OptionStepProductDTO dto) {
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
    private void updateTemporaryProductAtOptions(String registerId, OptionStepProductDTO dto) {
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);

        // 상품 수정

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
    private void addTemporaryProductAtDetails(DetailStepProductDTO dto) {
        TemporaryProduct newTemporaryProduct = dto.toEntity();
        temporaryProductRepository.save(newTemporaryProduct);
        Long temporaryProductId = newTemporaryProduct.getId();

        // 상품 상세 정보 저장
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, dto.getProductDetail());

        // 상품 클레임 정보 저장
        temporaryProductClaimService.addTemporaryProductClaim(temporaryProductId, dto.getClaim());

        // 상품 관련 연관 테이블 생성 (배송 지연 시간)
        deliveryTimeService.addTemporaryProductDeliveryTime(temporaryProductId, null);
    }

    @Transactional
    private void updateTemporaryProductAtDetails(String registerId, DetailStepProductDTO dto) {
        Long temporaryProductId = temporaryProductRepository.findIdByRegisterId(registerId);

        // 상품 상세 수정
        temporaryProductRepository.updateTemporaryProductDetail(temporaryProductId, dto.getProductDetail());

        // 상품 클레임 정보 수정
        temporaryProductClaimService.updateTemporaryProductClaim(temporaryProductId, dto.getClaim());
    }

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     *
     * @param productRequest
     * @return
     */
    private void validateProductRequest(
            List<String> productImageList,
            BasicStepProductDTO productRequest
    ) {
        Long subCategoryId = productRequest.getCategoryId();

        // 1. 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (productImageList.size() > 5) {
            throw new CustomException(ProductErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        // 2. 카테고리 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (!subCategoryService.existsBySubCategoryId(subCategoryId)) {
            throw new CustomException(CategoryErrorType.NOT_EXISTED_SUB_CATEGORY);
        }
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

        // 3. TemporaryProduct 의 대표 이미지 삭제
        attachFileService.deleteAttachFileByReferencedId(temporaryProductId, ReferencedEntityType.PRODUCT);

        // 4. 삭제
        temporaryProductRepository.deleteById(temporaryProductId);
    }

    @Override
    public TemporaryProductDTO findTemporaryProduct(Long userId) {
        try {
            TemporaryProduct temporaryProduct = findTemporaryProductByUserId(userId);
            TemporaryProductDTO dto = objectCopyHelper.copyObject(temporaryProduct, TemporaryProductDTO.class);
            Long temporaryProductId = temporaryProduct.getId();

            // TODO 존재하는 카테고리인지 확인하고, 존재하지 않는 카테고리이면 null 처리

            // TemporaryProductOption 값 가져오기
            List<TemporaryProductOptionDTO> options = temporaryProductOptionService.findTemporaryProductOptionByProductId(temporaryProductId)
                    .stream()
                    .map(option -> objectCopyHelper.copyObject(option, TemporaryProductOptionDTO.class))
                    .toList();
            dto.setTemporaryProductOptionDTO(options);

            // TemporaryProductDetailInfo 값 가져오기
            TemporaryProductDetailInfo detailInfo = temporaryProductDetailInfoService.findTemporaryProductDetailInfoByProductId(temporaryProductId);
            dto.setTemporaryDetailInfoDTO(objectCopyHelper.copyObject(detailInfo, TemporaryDetailInfoDTO.class));

            // TemporaryProductDeliveryTime 값 가져오기
            TemporaryProductDeliveryTime deliveryTime = deliveryTimeService.findTemporaryProductDeliveryTimeByTemporaryProductId(temporaryProductId);
            dto.setDeliveryTime(TemporaryProductDeliveryTimeDTO.toDTO(deliveryTime));

            // TemporaryClaimInfo 값 가져오기
            TemporaryProductClaimInfo claimInfo = temporaryProductClaimService.findTemporaryProductClaimByTemporaryProductId(temporaryProductId);
            dto.setClaim(objectCopyHelper.copyObject(claimInfo, ProductClaimInfoDTO.class));

            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
