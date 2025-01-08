package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ReviewErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.review.request.CreateReviewDTO;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.dto.review.response.ConsumerReviewDTO;
import com.impacus.maketplace.dto.review.response.ProductReviewDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.repository.review.ReviewRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public void addReview(Long userId, List<MultipartFile> images, CreateReviewDTO dto) {
        try {
            // 1. 유효성 검사
            validateReview(images, dto);

            // 2. 파일 업로드
            List<String> reviewImages = saveReviewImages(images);

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
    private void validateReview(List<MultipartFile> images, CreateReviewDTO dto) {
        // 1. 리뷰 이미지 유효성 검사
        if (images != null) {
            if (images.size() > 5) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "리뷰 이미지는 최대 5장까지 등록이 가능합니다.");
            }
            for (MultipartFile image : images) {
                if (image.getSize() > FileSizeConstants.REVIEW_PRODUCT_FILE_LIMIT) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "리뷰 이미지는 크기 제한을 초과하였습니다.");
                }
            }
        }

        // 2. 리뷰 평점 0.5 단위인지 확인
        if ((dto.getRating() * 2) % 1 != 0) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "평점은 0.5 단위로 등록가능합니다.");
        }

        // 3. 이미 존재하는 등록된 주문 상품에 리뷰 인지 확인
        if (reviewRepository.existsByOrderIdAndProductOptionId(dto.getOrderId(), dto.getProductOptionId())) {
            throw new CustomException(ReviewErrorType.EXISTED_REVIEW, "이미 등록된 리뷰가 존재합니다.");
        }

        // TODO 주문 확정된 주문인지 확인 (orderId, productOptionId)

        // TODO 비속어 검사

    }

    /**
     * 리뷰 이미지 저장 함수
     * 
     * @param images 리뷰 이미지
     */
    @Transactional
    public List<String> saveReviewImages(List<MultipartFile> images) {
        List<String> reviewImages = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                AttachFile reviewAttachImage = attachFileService.uploadFileAndAddAttachFile(
                        image, DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY
                );
                reviewImages.add(reviewAttachImage.getAttachFileName());
            }
        }

        return reviewImages;
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        try {
            reviewRepository.deleteReview(reviewId);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     */
    @Transactional
    public void restoreReview(Long reviewId) {
        try {
            reviewRepository.restoreReview(reviewId);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 상품 리뷰 조회
     *
     * @param productId 상품 ID
     * @param pageable 페이지네이션 객체
     * @return
     */
    public Page<ProductReviewDTO> findReviewsByProductId(Long productId, Pageable pageable) {
        try {
            return reviewRepository.findReviewsByProductId(productId, pageable);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 리뷰 수정
     *
     * @param reviewId
     * @param dto
     */
    @Transactional
    public void updateReview(Long reviewId, ReviewDTO dto) {
        try {
            // 1. 유효성 검사
            validateSavedReview(reviewId, dto);

            // 2. 리뷰 수정
            reviewRepository.updateReview(reviewId, dto);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 저장된 리뷰의 유효성 검사
     *
     * @param reviewId
     * @param dto
     */
    private void validateSavedReview(Long reviewId, ReviewDTO dto) {
        // 1. 존재하는 리뷰인지 확인
        if (!reviewRepository.existsByIdAndIsDeletedFalse(reviewId)) {
            throw new CustomException(ReviewErrorType.NOT_EXISTED_REVIEW_ID);
        }

        // 2. 리뷰 평점 0.5 단위인지 확인
        if ((dto.getRating() * 2) % 1 != 0) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "평점은 0.5 단위로 등록가능합니다.");
        }

        // 3. 리뷰 내용 비속어 확인 TODO
    }

    /**
     * 리뷰 이미지 수정
     *
     * @param reviewId
     * @param images
     */
    @Transactional
    public void updateReviewImages(Long reviewId, List<String> images) {
        try {
            // 1. 유효성 검사
            if (images != null) {
                if (images.size() > 5) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "리뷰 이미지는 최대 5장까지 등록이 가능합니다.");
                }
            }

            // 이미지 저장
            reviewRepository.updateReviewImages(reviewId, images);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 사용자 등록 리뷰 조회
     *
     * @param userId 리뷰 등록자 ID
     * @param pageable 페이지네이션 객체
     * @return
     */
    public Slice<ConsumerReviewDTO> findUserReviews(Long userId, Pageable pageable) {
        return reviewRepository.findUserReviews(userId, pageable);
    }

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
