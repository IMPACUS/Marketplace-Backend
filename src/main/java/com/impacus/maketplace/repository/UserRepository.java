package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.UserStatus;
import com.impacus.maketplace.entity.User;
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
    @Query("UPDATE User u SET u.status = :userStatus WHERE u.id = :userId")
    int updateUserStatus(@Param("userId") Long userId, @Param("userStatus") UserStatus userStatus);
}
