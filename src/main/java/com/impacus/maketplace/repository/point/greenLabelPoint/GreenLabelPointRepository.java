package com.impacus.maketplace.repository.point.greenLabelPoint;

import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenLabelPointRepository extends JpaRepository<GreenLabelPoint, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g.greenLabelPoint FROM GreenLabelPoint g WHERE g.userId = :userId")
    Long findWriteLockGreenLablePointByUserId(@Param("userId") Long userId);

    @Query("SELECT g.greenLabelPoint FROM GreenLabelPoint g where g.userId = :userId")
    Long findGreenLabelPointByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE GreenLabelPoint g " +
            "SET g.greenLabelPoint = :greenLabelPoint " +
            "WHERE g.userId = :userId")
    int updateGreenLabelPointByUserId(
            @Param("userId") Long userId,
            @Param("greenLabelPoint") Long greenLabelPoint
    );
}
