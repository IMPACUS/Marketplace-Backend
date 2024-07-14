package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;
import com.impacus.maketplace.repository.seller.SellerRepository;
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

    /**
     * 판매자 스토어 정보 변경 함수
     *
     * @param dto
     */
    public void updateBrandInformation(ChangeBrandInfoDTO dto, MultipartFile logoImage) {
        try {

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
