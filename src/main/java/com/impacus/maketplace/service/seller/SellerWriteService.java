package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.BrandRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerWriteService {
    private final SellerRepository sellerRepository;
    private final EmailService emailService;
    private final AttachFileService attachFileService;
    private final SellerService sellerService;
    private final BrandRepository brandRepository;
    private final BrandService brandService;

    /**
     * 판매자 스토어 정보 변경 함수
     * @param userId
     * @param dto
     * @param logoImage
     */
    @Transactional
    public void updateBrandInformation(
            Long userId,
            ChangeBrandInfoDTO dto,
            MultipartFile logoImage
    ) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);
            Long sellerId = seller.getId();

            // 1. 브랜드 정보 존재 확인
            boolean isExistedBrand = brandRepository.existsBySellerId(sellerId);

            // 2. 스토어 정보 변경
            sellerRepository.updateBrandInformationByUserId(userId, sellerId, dto, isExistedBrand);

            // 3. 브랜드 정보가 존재하지 않은 경우 생성
            if (!isExistedBrand) {
                brandService.saveBrand(dto.toEntity(sellerId));
            }

            // 4. 로고 이미지 변경
            Long logoImageId = seller.getLogoImageId();
            attachFileService.updateAttachFile(logoImageId, logoImage, DirectoryConstants.LOGO_IMAGE_DIRECTORY);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 담당자 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateManagerInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 정산 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateAdjustmentInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 로그인 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateLoginInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 택배사 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateDeliveryCompanyInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 배송지 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateDeliveryAddressInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
