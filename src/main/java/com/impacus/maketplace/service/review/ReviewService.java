package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.dto.review.ReviewSellerDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.repository.review.ReviewRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AttachFileService attachFileService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;

    private static final long TEXT_REVIEW_POINT = 200L;
    private static final long PHOTO_REVIEW_POINT = 350L;

    /**
     * 리뷰 생성
     *
     * @param images 리뷰 이미지
     * @param dto 리뷰 데이터
     * @return
     */
    @Transactional
    public void addReview(Long userId, List<MultipartFile> images, ReviewDTO dto) {
        try {
            // 1. 유효성 검사
            validateReview(images, dto);

            // 2. 파일 업로드
            Map<Long, String> reviewImages = saveReviewImages(images);


            // 3. 엔티티 저장
            Review review = dto.toEntity(userId, reviewImages);
            reviewRepository.save(review);

            // 4. 리뷰 포인트 지급
            greenLabelPointAllocationService.payGreenLabelPoint(
                    userId,
                    PointType.REVIEW,
                    images == null || images.isEmpty() ?
                        TEXT_REVIEW_POINT :
                        PHOTO_REVIEW_POINT
            );

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 리뷰 데이터 유효성 검사
     *
     * @param images
     */
    private void validateReview(List<MultipartFile> images, ReviewDTO dto) {
        // 1. 리뷰 이미지 유효성 검사
        if (images != null) {
            if (images.size() > 5) {
                new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "리뷰 이미지는 최대 5장까지 등록이 가능합니다.");
            }
            for (MultipartFile image : images) {
                if (image.getSize() > FileSizeConstants.REVIEW_PRODUCT_FILE_LIMIT) {
                    new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "리뷰 이미지는 크기 제한을 초과하였습니다.");
                }
            }
        }

        // TODO 주문 확정된 주문인지 확인

        // TODO 비속어 검사
    }

    /**
     * 리뷰 이미지 저장 함수
     * 
     * @param images 리뷰 이미지
     */
    @Transactional
    private Map<Long, String> saveReviewImages(List<MultipartFile> images) {
        Map<Long, String> reviewImages = new HashMap<>();

        if (images != null) {
            for (MultipartFile image : images) {
                AttachFile reviewAttachImage = attachFileService.uploadFileAndAddAttachFile(
                        image, DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY
                );
                reviewImages.put(reviewAttachImage.getId(), reviewAttachImage.getAttachFileName());
            }
        }

        return reviewImages;
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
