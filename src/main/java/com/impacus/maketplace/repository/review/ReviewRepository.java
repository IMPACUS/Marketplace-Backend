package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
    boolean existsByIdAndIsDeletedFalse(Long reviewId);
}
