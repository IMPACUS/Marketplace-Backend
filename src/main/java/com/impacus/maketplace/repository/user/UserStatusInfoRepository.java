package com.impacus.maketplace.repository.user;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.entity.user.UserStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusInfoRepository extends JpaRepository<UserStatusInfo, Long> {
    Optional<UserStatusInfo> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE UserStatusInfo u SET u.status = :status, u.statusReason = :statusReason WHERE u.userId = :id")
    int updateUserStatus(
            @Param("id") Long id,
            @Param("status") UserStatus status,
            @Param("statusReason") String statusReason
    );

    @Query("SELECT u.status FROM UserStatusInfo u WHERE u.userId = :userId")
    Optional<UserStatus> findProfileImageIdByUserId(@Param("userId") Long userId);
}
