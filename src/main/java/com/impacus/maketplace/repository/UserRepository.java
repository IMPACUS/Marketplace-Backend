package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmailLike(String emailWithPrefix);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.statusReason = :statusReason WHERE u.id = :id")
    int updateUserStatus(@Param("id") Long id, @Param("status") UserStatus status,
        @Param("statusReason") String statusReason);
}
