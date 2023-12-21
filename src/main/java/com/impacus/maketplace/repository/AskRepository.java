package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.common.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AskRepository extends JpaRepository<Ask,Long> {

    List<Ask> findAllByRegisterId(String registerId);
}
