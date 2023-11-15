package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByEmailLike(String emailWithPrefix);
}
