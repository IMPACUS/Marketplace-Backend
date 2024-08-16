package com.impacus.maketplace.service.qna;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.dto.qna.AddProductQuestionServiceDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.qna.ProductQuestion;
import com.impacus.maketplace.repository.qna.ProductQuestionRepository;
import com.impacus.maketplace.service.AttachFileService;
import com.impacus.maketplace.service.api.ProductInterface;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductQuestionService {

    private final ProductInterface productInterface;

    private final ProductQuestionRepository productQuestionRepository;

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
        ProductQuestion newQuestion = new ProductQuestion(dto.getProductId(), dto.getOrderId(), dto.getUserId(),
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
}
