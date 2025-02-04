package com.impacus.maketplace.service.qna;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.constants.FileSizeConstants;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.QuestionErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.qna.ProductQuestionSpec;
import com.impacus.maketplace.dto.qna.request.CreateQuestionDTO;
import com.impacus.maketplace.dto.qna.request.GetProductsParams;
import com.impacus.maketplace.dto.qna.response.SellerProductQuestionResponseDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.qna.Question;
import com.impacus.maketplace.repository.qna.QuestionCustomRepository;
import com.impacus.maketplace.repository.qna.QuestionRepository;
import com.impacus.maketplace.service.AttachFileService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionCustomRepository questionCustomRepository;
    private final AttachFileService attachFileService;

    /**
     * 상품 문의 등록
     */
    @Transactional
    public void addQuestion(
            Long userId,
            List<MultipartFile> images,
            CreateQuestionDTO dto
    ) {
        try {
            // 유효성 검사
            validateQuestion(images, dto);

            // 문의 이미지 저장
            List<String> reviewImages = saveQuestionImages(images);

            // 문의 저장
            Question question = dto.toEntity(userId, reviewImages);
            questionRepository.save(question);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    private void validateQuestion(List<MultipartFile> images, CreateQuestionDTO dto) {
        // 1. 문의 이미지 유효성 검사
        if (images != null) {
            if (images.size() > 5) {
                throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "문의 이미지는 최대 5장까지 등록이 가능합니다.");
            }
            for (MultipartFile image : images) {
                if (image.getSize() > FileSizeConstants.REVIEW_PRODUCT_FILE_LIMIT) {
                    throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "문의 이미지는 크기 제한을 초과하였습니다.");
                }
            }
        }

        // 2. 주문 상품에 이미 등록된 문의가 있는지 확인
        if (questionRepository.existsByOrderIdAndProductOptionIdAndIsDeletedFalse(
                dto.getOrderId(),
                dto.getProductOptionId()
        )) {
            throw new CustomException(QuestionErrorType.EXISTED_QUESTION, "이미 등록된 문의가 있습니다.");
        }

        // TODO 비속어 검사
    }

    /**
     * 리뷰 이미지 저장 함수
     *
     * @param images 리뷰 이미지
     */
    @Transactional
    public List<String> saveQuestionImages(List<MultipartFile> images) {
        List<String> questionImages = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                AttachFile reviewAttachImage = attachFileService.uploadFileAndAddAttachFile(
                        image, DirectoryConstants.QUESTION_PRODUCT_IMAGE_DIRECTORY
                );
                questionImages.add(reviewAttachImage.getAttachFileName());
            }
        }

        return questionImages;
    }


    /**
     * 문의 삭제
     */
    @Transactional
    public void deleteQuestion(long questionId) {
        if (questionRepository.existsByIdAndIsDeletedFalse(questionId)) {
            questionCustomRepository.deleteQuestionById(questionId);
        } else {
            throw new CustomException(QuestionErrorType.NOT_EXISTED_QUESTION_ID, "존재하지 않는 문의입니다.");
        }
    }

    public Page<SellerProductQuestionResponseDTO> getProducts(long sellerId, GetProductsParams params, Pageable pageable) {
        var spec = ProductQuestionSpec.builder()
                .sellerId(sellerId)
                .startDate(params.getStartDate())
                .endDate(params.getEndDate())
                .answered(params.getAnswered())
                .authorId(params.getAuthorId())
                .orderNumber(params.getOrderNumber())
                .build();
        Page<Question> result = questionCustomRepository.findByParams(spec, pageable);

        return result.map(SellerProductQuestionResponseDTO::fromEntity);
    }
}
