package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.SellerErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.seller.request.*;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.seller.delivery.SelectedSellerDeliveryAddress;
import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;
import com.impacus.maketplace.entity.seller.deliveryCompany.SelectedSellerDeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SellerDeliveryCompany;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.seller.BrandRepository;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.seller.delivery.SelectedSellerDeliveryAddressRepository;
import com.impacus.maketplace.repository.seller.deliveryCompany.SellerDeliveryCompanyRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.alarm.seller.AlarmSellerService;
import com.impacus.maketplace.service.seller.delivery.SelectedSellerDeliveryAddressService;
import com.impacus.maketplace.service.seller.delivery.SellerDeliveryAddressService;
import com.impacus.maketplace.service.seller.deliveryCompany.SelectedSellerDeliveryCompanyService;
import com.impacus.maketplace.service.seller.deliveryCompany.SellerDeliveryCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateSellerService {
    private final SellerRepository sellerRepository;
    private final AttachFileService attachFileService;
    private final ReadSellerService readSellerService;
    private final BrandRepository brandRepository;
    private final BrandService brandService;
    private final SellerBusinessInfoService sellerBusinessInfoService;
    private final SellerAdjustmentInfoService sellerAdjustmentInfoService;
    private final SellerDeliveryCompanyRepository sellerDeliveryCompanyRepository;
    private final UserService userService;
    private final SelectedSellerDeliveryCompanyService selectedSellerDeliveryCompanyService;
    private final SellerDeliveryCompanyService sellerDeliveryCompanyService;
    private final SellerDeliveryAddressService sellerDeliveryAddressService;
    private final SelectedSellerDeliveryAddressService selectedSellerDeliveryAddressService;
    private final SelectedSellerDeliveryAddressRepository selectedSellerDeliveryAddressRepository;
    private final EmailService emailService;
    private final AlarmSellerService alarmSellerService;

    /**
     * 판매자 입점 상태를 변경하는 함수
     *
     * @param sellerDTO
     * @return
     */
    @Transactional
    public Boolean changeEntryStatus(ChangeSellerEntryStatusDTO sellerDTO) {
        try {
            Long userId = sellerDTO.getUserId();
            EntryStatus entryStatus = sellerDTO.getEntryStatus();
            Integer charge = sellerDTO.getCharge();

            // 1. 유효성 검사
            User user = userService.findUserById(userId);
            Seller seller = readSellerService.findSellerByUserId(userId);
            validateSellerEntry(entryStatus, charge);

            // 2. 입점 상태 및 판매자 수수료 저장

            // 2-1 입정 승인 날짜 구하기
            // 승인: 현재 날짜로 업데이트
            // 거절: null로 변경
            LocalDateTime entryApprovedAt = null;
            if (entryStatus == EntryStatus.APPROVE) {
                entryApprovedAt = LocalDateTime.now();
            }
            seller.setEntryInformation(
                    entryStatus,
                    charge,
                    entryApprovedAt
            );

            EmailDto emailDto = EmailDto.builder()
                    .subject("입점 결과 메일 입니다.")
                    .receiveEmail(user.getEmail())
                    .build();

            // 3. Role 변경
            if (entryStatus == EntryStatus.APPROVE) {
                userService.updateUserType(userId, UserType.ROLE_APPROVED_SELLER);
                emailService.sendMail(emailDto, MailType.SELLER_APPROVE);
                alarmSellerService.saveDefault(seller.getId());
            } else {
                userService.updateUserType(userId, UserType.ROLE_UNAPPROVED_SELLER);
                emailService.sendMail(emailDto, MailType.SELLER_REJECT);
            }

        sellerRepository.save(seller);

            return true;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * Seller 입점 상태 변경 데이터 유효성 검사하는 함수
     */
    private void validateSellerEntry(EntryStatus entryStatus, Integer charge) {
        if (entryStatus == EntryStatus.APPROVE) {
            if (charge == null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "charge는 null이 될 수 없습니다.");
            }
        } else if (entryStatus == EntryStatus.REJECT) {
            if (charge != null) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "charge는 null 이여야 합니다.");
            }
        } else {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "entryStatus 데이터가 올바르지 않습니다.");
        }
    }

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
            Seller seller = readSellerService.findSellerByUserId(userId);
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
            Seller seller = readSellerService.findSellerByUserId(userId);
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
            Seller seller = readSellerService.findSellerByUserId(userId);
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
            sellerRepository.updateLoginInformationByUserId(userId, dto, dto.getNewPassword());
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
            Seller seller = readSellerService.findSellerByUserId(userId);
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
     * - deliveryAddressId 존재하지 않는 경우: 배송지 {count+1} 로 설정
     * - deliveryAddressId가 존재하는 경우: 배송지 데이터 수정
     * @param dto
     */
    @Transactional
    public void updateDeliveryAddressInformation(
            Long userId,
            ChangeSellerDeliveryAddressInfoDTO dto
    ) {

        try {
            Seller seller = readSellerService.findSellerByUserId(userId);
            Long sellerId = seller.getId();

            if (dto.getDeliveryAddressId() == null) {
                // 1. deliveryAddressId 존재하지 않는 경우
                SellerDeliveryAddress sellerDeliveryAddress = dto.toEntity(sellerId);
                sellerDeliveryAddressService.saveSellerDeliveryAddress(sellerDeliveryAddress);
            } else {
                // 2. deliveryAddressId가 존재하는 경우
                Long columnCnt = sellerRepository.updateDeliveryAddressInformationBySellerIdAndId(sellerId, dto);

                if (columnCnt <= 0L) {
                    throw new CustomException(SellerErrorType.NOT_EXISTED_SELLER);
                }
            }

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 메인 판매자 배송지를 변경하는 함수
     *
     * @param userId
     * @param sellerDeliveryAddressId
     */
    @Transactional
    public void updateMainDeliveryAddress(Long userId, Long sellerDeliveryAddressId) {
        try {
            Seller seller = readSellerService.findSellerByUserId(userId);
            Long sellerId = seller.getId();
            Optional<SelectedSellerDeliveryAddress> optionalAddress = selectedSellerDeliveryAddressRepository
                    .findBySellerId(sellerId);

            // 1. 유효성 검사
            if (!sellerDeliveryAddressService.existsSellerDeliveryAddressBySellerIdAndId(sellerId,
                    sellerDeliveryAddressId)) {
                throw new CustomException(SellerErrorType.NOT_EXISTED_SELLER_DELIVERY_ADDRESS_ID);
            }

            // 2. 업데이트
            if (optionalAddress.isPresent()) {
                // 2-1. 생성된 SelectedSellerDeliveryAddress가 존재하는 경우, 데이터 업데이트
                SelectedSellerDeliveryAddress address = optionalAddress.get();
                selectedSellerDeliveryAddressRepository.updateSellerDeliveryAddressIdById(address.getId(),
                        sellerDeliveryAddressId);
            } else {
                // 2-2 생성된 SelectedSellerDeliveryAddress가 존재하지 않는 경우, 생성
                SelectedSellerDeliveryAddress newAddress = SelectedSellerDeliveryAddress.toEntity(sellerId,
                        sellerDeliveryAddressId);
                selectedSellerDeliveryAddressService.saveSelectedSellerDeliveryAddress(newAddress);
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자 정보 수정 함수
     *
     * @param sellerId     정보 수정할 판매자 아이디
     * @param dto          변경할 정보
     * @param profileImage 변경될 프로필 이미지
     */
    @Transactional
    public void updateSellerInformation(
            Long sellerId,
            UpdateSellerInfoFromAdminDTO dto,
            MultipartFile profileImage
    ) {
        try {
            Seller seller = readSellerService.findSellerBySellerId(sellerId);

            // 1. 프로필 이미지 존재하는 경우, 프로필 이미지 저장
            Long profileImageId = null;
            if (profileImage != null) {
                profileImageId = attachFileService.uploadFileAndAddAttachFile(profileImage, DirectoryConstants.PROFILE_IMAGE_DIRECTORY).getId();
            }

            // 2. 판매자 정보 업데이트
            sellerRepository.updateSellerInformation(seller.getUserId(), sellerId, dto, profileImageId);

            // 판매자 상태가 변경됨에 따라 연관 데이터 업데이트
            switch (dto.getUserStatus()) {
                case ACTIVE -> {
                    handleActiveSellerStatus(sellerId);
                }
                case SUSPENDED -> {
                    handleSuspendedSellerStatus(sellerId);
                }
                case DEACTIVATED -> {
                    handleDeactivatedSellerStatus(sellerId);
                }
            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    private void handleActiveSellerStatus(Long sellerId) {

    }

    private void handleSuspendedSellerStatus(Long sellerId) {

    }

    private void handleDeactivatedSellerStatus(Long sellerId) {

    }
}
