package com.impacus.maketplace.repository.user;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.querydsl.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    Optional<User> findByEmailLikeAndIsDeletedFalse(String emailWithPrefix);

    Optional<User> findByEmailLikeAndIsDeletedTrue(String emailWithPrefix);

    @Query("SELECT u.id " +
            "FROM User u " +
            "WHERE u.email LIKE :emailWithPrefix and u.isDeleted = false")
    List<Long> findIdByEmailLike(@Param("emailWithPrefix") String emailWithPrefix);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.type = :type WHERE u.id = :id")
    int updateUserType(@Param("id") Long id, @Param("type") UserType type);

    @Query("SELECT u.profileImageId FROM User u WHERE u.id = :userId")
    Optional<Long> findProfileImageIdByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id AND u.type IN :types")
    boolean existsByIdAndType(
            @Param("id") Long id,
            @Param("types") List<UserType> types
    );

    long countByRecentLoginAtBetweenAndType(LocalDateTime start, LocalDateTime end, UserType type);
}
