package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.common.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFile, Long>, AttachFileCustomRepository {


}
