package com.impacus.maketplace.service.temporaryProduct;

import com.impacus.maketplace.common.enumType.ReferencedEntityType;
import com.impacus.maketplace.common.enumType.error.CategoryEnum;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.temporaryProduct.response.*;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDescription;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDetailInfo;
import com.impacus.maketplace.repository.temporaryProduct.TemporaryProductRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.BrandService;
import com.impacus.maketplace.service.category.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryProductService {
    private static final int PRODUCT_IMAGE_SIZE_LIMIT = 341172; // (1080 * 1053 * 3 = 3.41172MB 341172byte)
    private static final int PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT = 341172; // (1000 * 8000 * 3 = 24MB)
    private static final String TEMPORARY_PRODUCT_IMAGE_DIRECTORY = "temporaryProductImage";
    private static final String TEMPORARY_PRODUCT_DESCRIPTION_IMAGE_DIRECTORY = "temporaryProductDescriptionImage";
    private final TemporaryProductRepository temporaryProductRepository;
    private final AttachFileService attachFileService;
    private final BrandService brandService;
    private final TemporaryProductDescriptionService temporaryProductDescriptionService;
    private final TemporaryProductOptionService temporaryProductOptionService;
    private final TemporaryProductDetailInfoService temporaryProductDetailInfoService;
    private final ObjectCopyHelper objectCopyHelper;
    private final SubCategoryService subCategoryService;

    /**
     * TemporaryProduct 데이터가 사용자에게 등록되어 있는지 확인하는 함수
     *
     * @param userId
     * @return
     */
    public IsExistedTemporaryProductDTO checkIsExistedTemporaryProduct(Long userId) {
        try {
            boolean isExisted = temporaryProductRepository.existsByRegisterId(userId.toString());
            return new IsExistedTemporaryProductDTO(isExisted);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 임시 저장 상품 데이터를 등록 혹은 수정하는 함수
     *
     * @param productImageList
     * @param productRequest
     * @param productDescriptionImageList
     * @return
     */
    @Transactional
    public SimpleTemporaryProductDTO addOrModifyTemporaryProduct(
            Long sellerId,
            List<MultipartFile> productImageList,
            CreateProductDTO productRequest,
            List<MultipartFile> productDescriptionImageList) {
//        try {
            // 1. 임시 저장 상품이 존재하는지 확인
        Optional<TemporaryProduct> optionalData = temporaryProductRepository.findByRegisterId(sellerId.toString());
            if (optionalData.isPresent()) {
                // 임시 저장 상품 수정
                TemporaryProduct temporaryProduct = optionalData.get();
                return updateTemporaryProduct(temporaryProduct, productImageList, productRequest, productDescriptionImageList);
            } else {
                // 임시 저장 상품 저장
                return addTemporaryProduct(sellerId, productImageList, productRequest, productDescriptionImageList);
            }
//        } catch (Exception ex) {
//            throw new CustomException(ex);
//        }
    }

    /**
     * 새로운 임시 저장 상품 데이터를 저장하는 함수
     *
     * @param productImageList
     * @param productRequest
     * @param productDescriptionImageList
     * @return
     */
    @Transactional
    public SimpleTemporaryProductDTO addTemporaryProduct(
            Long sellerId,
            List<MultipartFile> productImageList,
            CreateProductDTO productRequest,
            List<MultipartFile> productDescriptionImageList
    ) {
        // 1. productRequest 데이터 유효성 검사
        validateProductRequest(productImageList, productRequest, productDescriptionImageList);

        // 2. Product 저장
        TemporaryProduct newTemporaryProduct = temporaryProductRepository.save(productRequest.toTemporaryEntity(sellerId));
        Long temporaryProductId = newTemporaryProduct.getId();

        // 3. 대표 이미지 저장 및 AttachFileGroup에 연관 관계 매핑 객체 생성
        productImageList
                .forEach(productImage -> {
                    try {
                        attachFileService.uploadFileAndAddAttachFile(
                                productImage,
                                TEMPORARY_PRODUCT_IMAGE_DIRECTORY,
                                temporaryProductId,
                                ReferencedEntityType.TEMPORARY_PRODUCT);
                    } catch (IOException e) {
                        throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                    }
                });

        // 4. Product description 저장
        TemporaryProductDescription temporaryProductDescription = temporaryProductDescriptionService.addTemporaryProductDescription(temporaryProductId, productRequest);

        // 5. 상품 설명 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
        productDescriptionImageList.stream()
                .map(productDescriptionImage -> {
                    try {
                        return attachFileService.uploadFileAndAddAttachFile(
                                productDescriptionImage,
                                TEMPORARY_PRODUCT_DESCRIPTION_IMAGE_DIRECTORY,
                                temporaryProductDescription.getId(),
                                ReferencedEntityType.TEMPORARY_PRODUCT_DESCRIPTION);
                    } catch (IOException e) {
                        throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                    }
                }).collect(Collectors.toList());

        // 6. Product option 저장
        temporaryProductOptionService.addTemporaryProductOption(temporaryProductId, productRequest.getProductOptions());

        // 7. Product detail 저장
        temporaryProductDetailInfoService.addTemporaryProductDetailInfo(temporaryProductId, productRequest.getProductDetail());

        return SimpleTemporaryProductDTO.toDTO(newTemporaryProduct);
    }

    /**
     * 전달받은 ProductRequest 의 유효성 검사를 하는 함수
     *
     * @param productRequest
     * @return
     */
    public void validateProductRequest(
            List<MultipartFile> productImageList,
            CreateProductDTO productRequest,
            List<MultipartFile> productDescriptionImageList) {

        Long subCategoryId = productRequest.getCategoryId();

        // 2. 상품 이미지 유효성 확인 (상품 이미지 크기 & 상품 이미지 개수)
        if (productImageList.size() > 5) {
            throw new CustomException(CommonErrorType.INVALID_PRODUCT, "상품 이미지 등록 가능 개수를 초과하였습니다.");
        }

        for (MultipartFile productImage : productImageList) {
            if (productImage.getSize() > PRODUCT_IMAGE_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        // 3. 상품 설명 이미지 크기 확인
        for (MultipartFile productImage : productDescriptionImageList) {
            if (productImage.getSize() > PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT) {
                throw new CustomException(CommonErrorType.INVALID_PRODUCT, "상품 이미지 크게가 큰 파일이 존재합니다.");
            }
        }

        if (!subCategoryService.existsBySubCategoryId(subCategoryId)) {
            throw new CustomException(CategoryEnum.NOT_EXISTED_SUB_CATEGORY);
        }
    }

    /**
     * 등록된 임시 상품 정보를 수정하는 API
     *
     * @param temporaryProduct
     * @param productImageList
     * @param productRequest
     * @param productDescriptionImageList
     * @return
     */
    @Transactional
    public SimpleTemporaryProductDTO updateTemporaryProduct(
            TemporaryProduct temporaryProduct,
            List<MultipartFile> productImageList,
            CreateProductDTO productRequest,
            List<MultipartFile> productDescriptionImageList) {
        try {
            // 1. productRequest 데이터 유효성 검사
            validateProductRequest(productImageList, productRequest, productDescriptionImageList);

            // 2. Product 수정
            temporaryProduct.setProduct(productRequest);
            temporaryProductRepository.save(temporaryProduct);
            Long temporaryProductId = temporaryProduct.getId();

            // 3. 대표 이미지 저장 및 AttachFileGroup과 연관 관계 매핑 객체 생성
            attachFileService.deleteAttachFileByReferencedId(temporaryProductId, ReferencedEntityType.TEMPORARY_PRODUCT);
            productImageList.stream()
                    .forEach(productImage -> {
                        try {
                            attachFileService.uploadFileAndAddAttachFile(
                                    productImage,
                                    TEMPORARY_PRODUCT_IMAGE_DIRECTORY,
                                    temporaryProductId,
                                    ReferencedEntityType.TEMPORARY_PRODUCT
                            );
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    });

            // 4. Product description 수정
            TemporaryProductDescription description = temporaryProductDescriptionService.findProductDescriptionByTemporaryProductId(temporaryProductId);
            description.setDescription(productRequest.getDescription());

            // 6. 상품 설명 이미지 저장 및 AttachFileGroup 에 연관 관계 매핑 객체 생성
            attachFileService.deleteAttachFileByReferencedId(description.getId(), ReferencedEntityType.TEMPORARY_PRODUCT_DESCRIPTION);
            productDescriptionImageList.stream()
                    .map(productDescriptionImage -> {
                        try {
                            return attachFileService.uploadFileAndAddAttachFile(
                                    productDescriptionImage,
                                    TEMPORARY_PRODUCT_DESCRIPTION_IMAGE_DIRECTORY,
                                    description.getId(),
                                    ReferencedEntityType.TEMPORARY_PRODUCT_DESCRIPTION
                            );
                        } catch (IOException e) {
                            throw new CustomException(CommonErrorType.FAIL_TO_UPLOAD_FILE);
                        }
                    }).collect(Collectors.toList());

            //8. Product option 수정
            temporaryProductOptionService.deleteAllTemporaryProductionOptionByTemporaryProductId(temporaryProductId);
            temporaryProductOptionService.addTemporaryProductOption(temporaryProductId, productRequest.getProductOptions());

            // 9. Product detail 수정
            TemporaryProductDetailInfo detailInfo = temporaryProductDetailInfoService.findTemporaryProductDetailInfoByProductId(temporaryProductId);
            detailInfo.setTemporaryProductDetailInfo(productRequest.getProductDetail());

            return SimpleTemporaryProductDTO.toDTO(temporaryProduct);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * userId로 TemporaryProduct를 찾는 함수
     *
     * @param userId
     * @return
     */
    public TemporaryProduct findTemporaryProductByUserId(Long userId) {
        return temporaryProductRepository.findByRegisterId(userId.toString())
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_TEMPORARY_PRODUCT));
    }

    /**
     * TemporaryProduct 삭제하는 함수
     * 1. TemporaryProductOption 삭제
     * 2. TemporaryProductDescription 이미지 삭제
     * 3. TemporaryProductDescription 삭제
     * 4. TemporaryProduct 대표 이미지 삭제
     * 5. TemporaryProduct 삭제
     *
     * @param userId
     */
    @Transactional
    public void deleteTemporaryProduct(Long userId) {
        // 1. TemporaryProduct 존재 확인
        TemporaryProduct deleteTemporaryProduct = findTemporaryProductByUserId(userId);
        Long temporaryProductId = deleteTemporaryProduct.getId();

        // 2. TemporaryProductOption 삭제
        temporaryProductOptionService.deleteAllTemporaryProductionOptionByTemporaryProductId(temporaryProductId);

        // 3. TemporaryProductDescription 이미지 삭제
        TemporaryProductDescription description = temporaryProductDescriptionService.findProductDescriptionByTemporaryProductId(temporaryProductId);
        attachFileService.deleteAttachFileByReferencedId(description.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION);

        // 4. TemporaryProductDescription 삭제
        temporaryProductDescriptionService.deleteTemporaryProductDescription(description);

        // 5. TemporaryProduct의 대표 이미지 삭제
        attachFileService.deleteAttachFileByReferencedId(temporaryProductId, ReferencedEntityType.PRODUCT);

        // 6. 삭제
        temporaryProductRepository.deleteById(temporaryProductId);
    }

    /**
     * TemporaryProduct를 조회하는 함수
     *
     * @param userId
     * @return
     */
    public TemporaryProductDTO findTemporaryProduct(Long userId) {
        try {
            TemporaryProduct temporaryProduct = findTemporaryProductByUserId(userId);
            TemporaryProductDTO dto = objectCopyHelper.copyObject(temporaryProduct, TemporaryProductDTO.class);
            Long temporaryProductId = temporaryProduct.getId();

            // TemporaryProductDescription 값 가져오기
            TemporaryProductDescription description = temporaryProductDescriptionService.findProductDescriptionByTemporaryProductId(temporaryProductId);
            dto.setDescription(description.getDescription());

            // TemporaryProductOption 값 가져오기
            List<TemporaryProductOptionDTO> options = temporaryProductOptionService.findTemporaryProductOptionByProductId(temporaryProductId)
                    .stream()
                    .map(option -> objectCopyHelper.copyObject(option, TemporaryProductOptionDTO.class))
                    .toList();
            dto.setTemporaryProductOptionDTO(options);

            // TemporaryProductDescription 값 가져오기
            TemporaryProductDetailInfo detailInfo = temporaryProductDetailInfoService.findTemporaryProductDetailInfoByProductId(temporaryProductId);
            dto.setTemporaryDetailInfoDTO(objectCopyHelper.copyObject(detailInfo, TemporaryDetailInfoDTO.class));

            // 대표이미지 데이터 가져오기
            List<AttachFileDTO> attachFileDTOS = attachFileService.findAllAttachFile(description.getId(), ReferencedEntityType.PRODUCT_DESCRIPTION)
                    .stream().map(attachFile -> new AttachFileDTO(attachFile.getId(), attachFile.getAttachFileName()))
                    .toList();
            dto.setProductImageList(attachFileDTOS);

            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
