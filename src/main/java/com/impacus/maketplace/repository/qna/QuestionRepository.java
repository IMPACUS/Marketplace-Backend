package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.entity.qna.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByOrderIdAndProductOptionIdAndIsDeletedFalse(Long orderId, Long productOptionId);

    /**
     * {@link Question#getUserId()} 기반 권한 체크 후 삭제
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'PRINCIPAL_ADMIN') or #entity.userId == @securityUtils.currentUserId")
    default void deleteWithAuthority(@Param("entity") Question entity) {
        delete(entity);
    }

}