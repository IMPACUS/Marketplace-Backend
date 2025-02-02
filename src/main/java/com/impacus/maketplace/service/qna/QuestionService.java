package com.impacus.maketplace.service.qna;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.dto.qna.AddProductQuestionServiceDTO;
import com.impacus.maketplace.dto.qna.ProductQuestionSpec;
import com.impacus.maketplace.dto.qna.request.GetProductsParams;
import com.impacus.maketplace.dto.qna.response.SellerProductQuestionResponseDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.qna.Question;
import com.impacus.maketplace.repository.qna.QuestionCustomRepository;
import com.impacus.maketplace.repository.qna.QuestionRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.api.ProductInterface;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {

    private final ProductInterface productInterface;

    private final QuestionRepository productQuestionRepository;

    private final QuestionCustomRepository productQuestionCustomRepository;

    private final AttachFileService attachFileService;

    /**
     * 상품 문의 등록
     */
    @Transactional
    public void addProductQuestion(AddProductQuestionServiceDTO dto) {
        productInterface.checkExistenceById(dto.getProductId());
        // 1. 첨부파일 업로드
        AttachFile attachFile = attachFileService.uploadFileAndAddAttachFile(dto.getImage(), DirectoryConstants.PRODUCT_QUESTION_DIRECTORY);
        // 2. 문의 entity 저장
        Question newQuestion = new Question(dto.getProductId(), dto.getOrderId(), dto.getUserId(),
                dto.getContents(), attachFile.getId());
        productQuestionRepository.save(newQuestion);
    }

    /**
     * 문의 삭제
     */
    @Transactional
    public void deleteProductQuestionById(long questionId) {
        productQuestionRepository.findById(questionId)
                .ifPresent(productQuestionRepository::deleteWithAuthority);
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
        Page<Question> result = productQuestionCustomRepository.findByParams(spec, pageable);

        return result.map(SellerProductQuestionResponseDTO::fromEntity);
    }
}
