package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.seller.request.*;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.seller.deliveryCompany.SelectedSellerDeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SellerDeliveryCompany;
import com.impacus.maketplace.repository.seller.BrandRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.seller.deliveryCompany.SellerDeliveryCompanyRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.seller.deliveryCompany.SelectedSellerDeliveryCompanyService;
import com.impacus.maketplace.service.seller.deliveryCompany.SellerDeliveryCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerWriteService {
    private final SellerRepository sellerRepository;
    private final AttachFileService attachFileService;
    private final SellerService sellerService;
    private final BrandRepository brandRepository;
    private final BrandService brandService;
    private final SellerBusinessInfoService sellerBusinessInfoService;
    private final SellerAdjustmentInfoService sellerAdjustmentInfoService;
    private final SellerDeliveryCompanyRepository sellerDeliveryCompanyRepository;
    private final UserService userService;
    private final SelectedSellerDeliveryCompanyService selectedSellerDeliveryCompanyService;
    private final SellerDeliveryCompanyService sellerDeliveryCompanyService;

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
    public void updateManagerInformation(
            Long userId,
            ChangeSellerManagerInfoDTO dto,
            MultipartFile businessRegistrationImage,
            MultipartFile mailOrderBusinessReportImage
    ) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);
            BusinessType businessType = seller.getBusinessType();
            Long sellerId = seller.getId();
            SellerBusinessInfo sellerBusinessInfo = sellerBusinessInfoService.findSellerBusinessInfoBySellerId(sellerId);

            // 1. 유효성 검사
            validateChangeSellerManagerInfoDTO(
                    businessType,
                    dto.getMailOrderBusinessReportNumber(),
                    mailOrderBusinessReportImage
            );

            // 2. 담당자 정보 변경
            sellerRepository.updateManagerInformationBySellerId(sellerId, dto);

            // 3. 사업자 등록 번호 사본 변경
            Long copyBusinessRegistrationCertificateId = sellerBusinessInfo.getCopyBusinessRegistrationCertificateId();
            attachFileService.updateAttachFile(
                    copyBusinessRegistrationCertificateId,
                    businessRegistrationImage,
                    DirectoryConstants.COPY_FILE_DIRECTORY
            );

            // 4. 통신 판매업 번호 사본 변경
            if (businessType != BusinessType.SIMPLIFIED_TAXABLE_PERSON) {
                Long copyMainOrderBusinessReportCardId = sellerBusinessInfo.getCopyMainOrderBusinessReportCardId();
                attachFileService.updateAttachFile(
                        copyMainOrderBusinessReportCardId,
                        mailOrderBusinessReportImage,
                        DirectoryConstants.LOGO_IMAGE_DIRECTORY
                );
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * ChangeSellerManagerInfoDTO 유효성 검사
     *
     * @param businessType
     * @param mailOrderBusinessReportNumber
     */
    private void validateChangeSellerManagerInfoDTO(
            BusinessType businessType,
            String mailOrderBusinessReportNumber,
            MultipartFile mailOrderBusinessReportImage
    ) {
        if (businessType == BusinessType.SIMPLIFIED_TAXABLE_PERSON) {
            if (mailOrderBusinessReportImage != null || mailOrderBusinessReportNumber != null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "간이 과세자인 경우, 통신판매업신고증관련 값이 null 이여야 합니다.");
            }
        } else {
            if (mailOrderBusinessReportImage == null || mailOrderBusinessReportNumber == null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "간이 과세자가 아닌 경우, 통신판매업신고증관련 값이 null 이면 안됩니다.");
            }
        }
    }

    /**
     * 판매자 정산 정보 변경 함수
     *
     * @param dto
     */
    @Transactional
    public void updateAdjustmentInformation(
            Long userId,
            ChangeSellerAdjustmentInfoDTO dto,
            MultipartFile bankBookImage
    ) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);
            Long sellerId = seller.getId();
            SellerAdjustmentInfo adjustmentInfo = sellerAdjustmentInfoService.findSellerAdjustmentInfoBySellerId(sellerId);

            // 2. 정산 정보 변경
            sellerRepository.updateAdjustmentInformationBySellerId(sellerId, dto);

            // 3. 통장 사본 이미지 변경
            Long copyBankBookId = adjustmentInfo.getCopyBankBookId();
            attachFileService.updateAttachFile(
                    copyBankBookId,
                    bankBookImage,
                    DirectoryConstants.COPY_FILE_DIRECTORY
            );
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
    public void updateLoginInformation(Long userId, ChangeSellerLoginInfoDTO dto) {
        try {
            // 1. 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(dto.getNewPassword()))) {
                throw new CustomException(CommonErrorType.INVALID_PASSWORD);
            }

            // 2. 판매자 로그인 정보 변경
            String encodedPassword = userService.encodePassword(dto.getNewPassword());
            sellerRepository.updateLoginInformationByUserId(userId, dto, encodedPassword);
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
    public void updateDeliveryCompanyInformation(Long userId, ChangeSellerDeliveryCompanyInfoDTO dto) {
        try {
            Seller seller = sellerService.findSellerByUserId(userId);
            Long sellerId = seller.getId();
            Optional<SellerDeliveryCompany> optionalSellerDeliveryCompany = sellerDeliveryCompanyRepository.findBySellerId(sellerId);
            List<DeliveryCompany> deliveryCompanies = dto.getDeliveryCompanies();

            // 유효성 검사
            if (deliveryCompanies.contains(DeliveryCompany.NONE)) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "deliveryCompany에 유효하지 않는 데이터 존재합니다.");
            }
            if (hasDuplicatedDeliveryCompany(deliveryCompanies)) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "deliveryCompany에 중복된 데이터가 존재합니다.");
        }

            // 택배사 정보 업데이트
            if (optionalSellerDeliveryCompany.isPresent()) {
                // 1-1. SellerDeliveryCompany 업데이트
                sellerRepository.updateDeliveryCompanyInformationBySellerId(sellerId, dto);

                // 1-2. SelectedSellerDeliveryCompany 업데이트
                selectedSellerDeliveryCompanyService.updateAllSelectedSellerDeliveryCompany(
                        optionalSellerDeliveryCompany.get().getId(),
                        deliveryCompanies
                );


            } else {
                // 2-1. SellerDeliveryCompany 저장
                SellerDeliveryCompany company = dto.toEntity(sellerId);
                sellerDeliveryCompanyService.saveSellerDeliveryCompany(company);

                // 2-2. SelectedSellerDeliveryCompany 저장
                List<SelectedSellerDeliveryCompany> companies = dto.toSelectedEntity(company.getId());
                selectedSellerDeliveryCompanyService.saveAllSelectedSellerDeliveryCompany(companies);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * DeliveryCompany 리스트에서 중복된 정보가 있는지 확인합니다.
     *
     * @param deliveryCompanies 중복을 확인할 DeliveryCompany 리스트
     * @return 중복이 있으면 true, 없으면 false
     */
    private boolean hasDuplicatedDeliveryCompany(List<DeliveryCompany> deliveryCompanies) {
        Set<String> nameSet = new HashSet<>();

        for (DeliveryCompany company : deliveryCompanies) {
            if (!nameSet.add(company.name())) {
                return true;
            }
        }

        return false;
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
