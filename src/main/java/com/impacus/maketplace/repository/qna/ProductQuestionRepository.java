package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.entity.qna.ProductQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {

    List<ProductQuestion> findByUserId(long userId);

    Page<ProductQuestion> findByProductId(long productId, Pageable pageable);

}