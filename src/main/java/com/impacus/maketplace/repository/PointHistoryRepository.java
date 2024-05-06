package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, PointHistoryCustomRepository {

    List<PointHistory> findByPointMasterId(Long pointMasterId);

    /**
     * pointHistory 테이블에서 creatAt이 현재시간보다 6개월 전인 것 조회
     */
    List<PointHistory> findByCreateAtGreaterThanEqualAndExpiredAtIsNotNullAndExpiredCheckIsFalse(LocalDateTime xMonthAgo);

    /**
     * PointHistory 테이블에서 changePoint가 있고, PointType 이 EXPIRE 아닌 것 또는
     * PointHistory 테이블에서 6개월간 USE, SAVE 기록이 없는 회원 조회
     */

}
