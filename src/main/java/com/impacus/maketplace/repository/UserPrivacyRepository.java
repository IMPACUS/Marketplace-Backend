package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivacyRepository extends JpaRepository<UserPrivacy, Long> {
}
