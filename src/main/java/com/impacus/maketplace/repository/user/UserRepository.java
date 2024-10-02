package com.impacus.maketplace.repository.user;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.querydsl.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    List<User> findByEmailLike(String emailWithPrefix);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

//    List<User> findByRecentLoginAtBeforeAndFirstDormancyIsFalse(LocalDateTime fiveMonthAgo); // 1차 휴면이 false 이고, 마지막로그인 후 5개월찾기
//
//    List<User> findByUpdateDormancyAtAndFirstDormancyIsTrueOrSecondDormancyIsTrue(LocalDate nowDate);

    @Modifying
    @Query("UPDATE User u SET u.type = :type WHERE u.id = :id")
    int updateUserType(@Param("id") Long id, @Param("type") UserType type);

    @Query("SELECT u.profileImageId FROM User u WHERE u.id = :userId")
    Optional<Long> findProfileImageIdByUserId(@Param("userId") Long userId);

}
