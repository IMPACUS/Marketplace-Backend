package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.dto.review.ReviewSellerDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.repository.review.ReviewRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AttachFileService attachFileService;

    /**
     * 리뷰 생성
     *
     * @param images 리뷰 이미지
     * @param dto 리뷰 데이터
     * @return
     */
    @Transactional
    public void addReview(List<MultipartFile> images, ReviewDTO dto) {
        try {
            // 1. 유효성 검사
            validateReview(images);

            // 2. 파일 업로드
            AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(images, DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY);

            // 3. 엔티티 저장
            Review review = Review
                    .builder()
                    .orderId(dto.getOrderId()) // 1
                    .score(dto.getScore()) // 5
                    .buyerUploadImgId(logoImageFile.getId()) // 백엔드에서 가공 처리
                    .sellerId(dto.getSellerId()) // 2
                    .buyerContents(dto.getBuyerContents()) // 리뷰 테스트
                    .build();
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 리뷰 데이터 유효성 검사
     * @param images
     */
    private void validateReview(List<MultipartFile> images) {
        // 1. 리뷰 이미지 유효성 검사
        if (images.getSize() > FileSizeConstants.REVIEW_PRODUCT_FILE_LIMIT) {
            new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "이미지 크기가 제한을 넘었습니다.");
        }

        // 주문 확정된 주문인지 확인

        // 비속어 검사
    }

//    /**
//     * 구매자 관점 - 리스트 조회
//     * @param userId
//     * @return
//     */
//    @Transactional(readOnly = true)
//    public List<ReviewBuyerDTO> displayBuyersReviewList(Long userId) {
//        return reviewRepository.displayViewBuyerReview(userId);
//    }

//    /**
//     * 리뷰 한개만 조회
//     * @param userId
//     * @param orderId
//     * @return
//     */
//    @Transactional(readOnly = true)
//    public ReviewBuyerDTO displayBuyersReviewOne(Long userId, Long orderId) {
//        return reviewRepository.displayViewBuyerReviewOne(userId, orderId);
//    }
//
//    /**
//     * 판매자용 리뷰 리스트 작성
//     * @param pageable
//     * @param userId
//     * @Param search
//     * @return
//     */
//    @Transactional(readOnly = true)
//    public Slice<ReviewSellerDTO> displaySellerReviewList(Pageable pageable, Long userId, String search) {
//        return reviewRepository.displaySellerReviewList(pageable, userId, search);
//    }
//
//    /**
//     * 판미자용 리뷰 상세 중 답글 달기
//     * @param review
//     * @return
//     */
//    @Transactional
//    public Review writeSellerComment(Review review) {
//        Long id = review.getId();
//        String comment = review.getSellerComment();
//
//        Review newReview = reviewRepository.findById(id).orElse(null);
//        newReview.setSellerComment(comment);
//        newReview.setIsComment(true);
//        // 여기서 review 객체를 수정할 수 있습니다.
//        return reviewRepository.save(review);
//    }
}
