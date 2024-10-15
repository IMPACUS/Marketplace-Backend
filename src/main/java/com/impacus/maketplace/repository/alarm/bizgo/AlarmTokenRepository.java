package com.impacus.maketplace.repository.alarm.bizgo;

import com.impacus.maketplace.entity.alarm.token.AlarmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AlarmTokenRepository extends JpaRepository<AlarmToken, Long> {
    @Query("SELECT a FROM AlarmToken a WHERE a.type = 'BIZGO'")
    Optional<AlarmToken> findBizgoToken();

    @Modifying
    @Query("UPDATE AlarmToken a SET a.token = :token, a.expiredDate = :expiredDate, a.modifyAt = :modifyAt WHERE a.id = :id")
    void updateToken(@Param("token") String token, @Param("expiredDate") LocalDateTime expiredDate, @Param("modifyAt") LocalDateTime modifyAt, @Param("id") Long id);

    Optional<AlarmToken> findByUserId(Long userId);
}
