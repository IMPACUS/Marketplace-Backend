package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.entity.review.ReviewReply;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
    boolean existsByReviewId(Long reviewId);

    boolean existsById(Long reviewReplyId);

    @Transactional
    @Modifying
    @Query("UPDATE ReviewReply rr SET rr.contents = :contents WHERE rr.id = :id")
    int updateContentsById(@Param("id") Long id, @Param("contents") String contents);

}
