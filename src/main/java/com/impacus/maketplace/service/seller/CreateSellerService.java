package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.seller.request.CreateSellerDTO;
import com.impacus.maketplace.dto.seller.response.SimpleSellerFromSellerDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.EmailService;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.alarm.seller.AlarmSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateSellerService {
    private final SellerRepository sellerRepository;
    private final SellerBusinessInfoService sellerBusinessInfoService;
    private final SellerAdjustmentInfoService sellerAdjustmentInfoService;
    private final AttachFileService attachFileService;
    private final UserService userService;
    private final EmailService emailService;
    private final AlarmSellerService alarmSellerService;

    /**
     * Seller를 저장하는 함수
     *
     * @param seller
     * @return
     */
    @Transactional
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    /**
     * 판매자를 생성하는 함수
     *
     * @param sellerDTO
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     * @return
     */
    @Transactional
    public SimpleSellerFromSellerDTO addSeller(CreateSellerDTO sellerDTO,
                                               MultipartFile logoImage,
                                               MultipartFile businessRegistrationImage,
                                               MultipartFile mailOrderBusinessReportImage,
                                               MultipartFile bankBookImage) {
        try {
            String email = sellerDTO.getEmail();
            String password = sellerDTO.getPassword();

            // 1. 유효성 검사
            validateSellerInput(
                    sellerDTO,
                    logoImage,
                    businessRegistrationImage,
                    mailOrderBusinessReportImage,
                    bankBookImage);

            // 2. 로고 이미지, 사업자 등록증 사본, 통신판매업신고증사본, 통장 사본 저장
            AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(logoImage, DirectoryConstants.LOGO_IMAGE_DIRECTORY);
            AttachFile businessRegistrationFile = attachFileService.uploadFileAndAddAttachFile(businessRegistrationImage, DirectoryConstants.COPY_FILE_DIRECTORY);
            AttachFile mailOrderBusinessReportFile = mailOrderBusinessReportImage == null ? null : attachFileService.uploadFileAndAddAttachFile(mailOrderBusinessReportImage, DirectoryConstants.COPY_FILE_DIRECTORY);
            AttachFile bankBookFile = attachFileService.uploadFileAndAddAttachFile(bankBookImage, DirectoryConstants.COPY_FILE_DIRECTORY);

            // 3. User 저장
            User user = new User(
                    email,
                    userService.encodePassword(password),
                    email,
                    sellerDTO.getContactNumber(),
                    UserType.ROLE_UNAPPROVED_SELLER);
            userService.saveUser(user);

            // 4. Seller 저장
            Seller seller = saveSeller(sellerDTO.toSellerEntity(user.getId(), logoImageFile.getId()));
            Long sellerId = seller.getId();

            // 5. SellerBusinessInfo 저장
            SellerBusinessInfo sellerBusinessInfo = sellerDTO.toSellerBusinessInfoEntity(sellerId,
                    businessRegistrationFile.getId(),
                    mailOrderBusinessReportFile == null ? null : mailOrderBusinessReportFile.getId());
            sellerBusinessInfoService.saveSellerBusinessInfo(sellerBusinessInfo);

            // 6. SellerAdjustmentInfo 저장
            SellerAdjustmentInfo adjustmentInfo = sellerDTO.toSellerAdjustmentInfo(sellerId, bankBookFile.getId());
            sellerAdjustmentInfoService.saveSellerAdjustmentInfo(adjustmentInfo);
            alarmSellerService.saveDefault(sellerId);

            return SimpleSellerFromSellerDTO.toDTO(user.getId(), seller);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * CreateSellerDTO 유효성 검사 하는 함수
     *
     * @param sellerDTO
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     */
    private void validateSellerInput(
            CreateSellerDTO sellerDTO,
            MultipartFile logoImage,
            MultipartFile businessRegistrationImage,
            MultipartFile mailOrderBusinessReportImage,
            MultipartFile bankBookImage) {
        String email = sellerDTO.getEmail();
        String password = sellerDTO.getPassword();

        // 1. 이메일 유효성 검사
        if (userService.existUserByEmail(email)) {
            throw new CustomException(CommonErrorType.DUPLICATED_EMAIL);
        }

        // 2. 비밃번호 유효성 검사
        if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
            throw new CustomException(CommonErrorType.INVALID_PASSWORD);
        }

        // 3. 상세 데이터 유효성 검사
        validateBusinessType(sellerDTO.getBusinessType(), mailOrderBusinessReportImage, sellerDTO.getMailOrderBusinessReportNumber());
        validateFileLimit(logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);
    }

    /**
     * Seller의 BusinessType 유효성 검사하는 함수
     */
    private void validateBusinessType(
            BusinessType businessType,
            MultipartFile mailOrderBusinessReportImage,
            String mailOrderBusinessReportNumber
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

    private void validateFileLimit(MultipartFile logoImage,
                                   MultipartFile businessRegistrationImage,
                                   MultipartFile mailOrderBusinessReportImage,
                                   MultipartFile bankBookImage) {
        if (logoImage.getSize() > FileSizeConstants.LOGO_IMAGE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "로고 이미지 크기가 제한을 넘었습니다.");
        }

        if (businessRegistrationImage.getSize() > FileSizeConstants.SELLER_FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "사업자 등록증 사본의 이미지 크기가 제한을 넘었습니다.");
        }

        if (mailOrderBusinessReportImage != null && mailOrderBusinessReportImage.getSize() > FileSizeConstants.SELLER_FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "통신판매업신고증 사본의 크기가 제한을 넘었습니다.");
        }

        if (bankBookImage.getSize() > FileSizeConstants.SELLER_FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "통장 사본의 이미지 크기가 제한을 넘었습니다.");
        }

    }
}
