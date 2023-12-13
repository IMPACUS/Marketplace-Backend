package com.impacus.maketplace.repository;

import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.entity.common.EmailHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory,Long> {

    @Query("SELECT e.authNo FROM EmailHistory e " +
            "WHERE e.receiveEmail = :receiveEmail " +
            "  AND e.authNo = :authNo " +
            "  AND e.sendDatetime > (CURRENT_TIMESTAMP - INTERVAL 3 MINUTE)")
    List<String> findAuthNoByReceiveEmailAndAuthNoAndSendDatetime(@Param("receiveEmail") String receiveEmail,
                                                                  @Param("authNo") String authNo);
//    List<String> findByReceiveEmailAndAuthNoAndSendDatetimeGreaterThan(String receiveEmail, String authNo, LocalDateTime threeMinutesAgo);

}
