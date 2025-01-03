package com.impacus.maketplace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.impacus.maketplace.repository.review.ReviewRepository;
import com.impacus.maketplace.service.review.ReviewService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewServiceTest {
    @Mock
    private AttachFileService attachFileService;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService; // ReviewService는 실제 테스트 대상
//
//    @Test
//    @Transactional
//    public void testDoWriteReview() throws IOException {
//        // Mock data
//        MockMultipartFile reviewProductImage = new MockMultipartFile(
//                "reviewProductImage",
//                "test.jpg",
//                "image/jpeg",
//                "test image content".getBytes()
//        );
//
//        ReviewDTO reviewDTO = new ReviewDTO();
//        reviewDTO.setOrderId(1L);
//        reviewDTO.setScore(5);
//        reviewDTO.setBuyerUploadImgId(1L);
//        reviewDTO.setSellerId(2L);
//        reviewDTO.setBuyerContents("리뷰 테스트");
//
//        AttachFile mockFile = new AttachFile();
//        mockFile.setId(2L);
//
//        Review mockReview = Review.builder()
//                .orderId(reviewDTO.getOrderId())
//                .score(reviewDTO.getScore())
//                .buyerUploadImgId(mockFile.getId())
//                .sellerId(reviewDTO.getSellerId())
//                .buyerContents(reviewDTO.getBuyerContents())
//                .build();
//
//        // Mock the attachFileService behavior
//        when(attachFileService.uploadFileAndAddAttachFile(any(MultipartFile.class), eq(DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY)))
//                .thenReturn(mockFile);
//
//        // Mock the reviewRepository behavior
//        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
//
//        // Call the service method
//        Review result = reviewService.doWriteReview(reviewProductImage, reviewDTO);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(reviewDTO.getOrderId(), result.getOrderId());
//        assertEquals(reviewDTO.getScore(), result.getScore());
//        assertEquals(mockFile.getId(), result.getBuyerUploadImgId());
//        assertEquals(reviewDTO.getSellerId(), result.getSellerId());
//        assertEquals(reviewDTO.getBuyerContents(), result.getBuyerContents());
//    }
}
