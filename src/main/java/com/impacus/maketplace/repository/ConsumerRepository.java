package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.consumer.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    boolean existsByUserId(Long userId);

    @Modifying
    @Query("UPDATE Consumer c SET c.ci = :ci, c.modifyAt = :modifyAt WHERE c.userId = :userId")
    void updateConsumer(@Param("userId") Long userId, @Param("ci") String ci, @Param("modifyAt") LocalDateTime modifyAt);
}
