package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmailLike(String emailWithPrefix);

    Optional<User> findByEmail(String email);
}
