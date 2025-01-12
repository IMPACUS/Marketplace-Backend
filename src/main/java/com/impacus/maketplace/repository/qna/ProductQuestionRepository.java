package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.entity.qna.ProductQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {

    List<ProductQuestion> findByUserId(long userId);

    Page<ProductQuestion> findByProductId(long productId, Pageable pageable);

    /**
     * {@link ProductQuestion#getUserId()} 기반 권한 체크 후 삭제
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'PRINCIPAL_ADMIN') or #entity.userId == @securityUtils.currentUserId")
    default void deleteWithAuthority(@Param("entity") ProductQuestion entity) {
        delete(entity);
    }

}