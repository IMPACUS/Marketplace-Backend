package com.impacus.maketplace.repository.alarm.bizgo;

import com.impacus.maketplace.entity.alarm.bizgo.BizgoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BizgoRepository extends JpaRepository<BizgoToken, Long> {
    @Query("SELECT b FROM BizgoToken b WHERE b.id = (SELECT MAX(b2.id) FROM BizgoToken b2)")
    Optional<BizgoToken> latestToken();
}
