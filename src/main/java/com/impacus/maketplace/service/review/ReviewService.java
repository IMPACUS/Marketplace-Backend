package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.repository.review.ReviewRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AttachFileService attachFileService;

    @Transactional(readOnly = true)
    public List<ReviewBuyerDTO> displayBuyersReviewList(Long userId, Long orderId) {
        return reviewRepository.displayViewBuyerReview(userId, orderId);
    }

    @Transactional
    public Review doWriteReview(MultipartFile reviewProductImage, ReviewDTO reviewDTO) {
        try {
            // 1. 파일 사이즈 크기 유효성 검사 (5mb 정도)
            if (reviewProductImage.getSize() > FileSizeConstants.REVIEW_PRODUCT_FILE_LIMIT) {
                new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "이미지 크기가 제한을 넘었습니다.");
            }

            // 2. 파일 업로드!!
            AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(reviewProductImage, DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY);

            // 3. 엔티티 저장

            Review review = Review
                    .builder()
                    .orderId(reviewDTO.getOrderId()) // 1
                    .score(reviewDTO.getScore()) // 5
                    .buyerUploadImgId(logoImageFile.getId()) // 백엔드에서 가공 처리
                    .sellerId(reviewDTO.getSellerId()) // 2
                    .buyerContents(reviewDTO.getBuyerContents()) // 리뷰 테스트
                    .build();

            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
