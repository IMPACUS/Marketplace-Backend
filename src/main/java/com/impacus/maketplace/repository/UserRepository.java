package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmailLike(String emailWithPrefix);

    Optional<User> findByEmail(String email);

    List<User> findByRecentLoginAtBeforeAndFirstDormancyIsFalse(LocalDateTime fiveMonthAgo); // 1차 휴면이 false 이고, 마지막로그인 후 5개월찾기

    List<User> findByUpdateDormancyAtAndFirstDormancyIsTrueOrSecondDormancyIsTrue(LocalDate nowDate);
    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.statusReason = :statusReason WHERE u.id = :id")
    int updateUserStatus(@Param("id") Long id, @Param("status") UserStatus status,
        @Param("statusReason") String statusReason);



}
