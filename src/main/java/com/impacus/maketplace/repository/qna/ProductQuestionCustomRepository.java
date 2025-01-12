package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.dto.qna.ProductQuestionSpec;
import com.impacus.maketplace.entity.qna.ProductQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQuestionCustomRepository {
    Page<ProductQuestion> findByParams(ProductQuestionSpec spec, Pageable pageable);
}
